package com.example.myweather;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.example.myweather.weather.Weather;
import com.example.myweather.weather.WeatherInterface;
import com.example.myweather.weather.WeatherNow;
import com.example.myweather.weather.WeatherNowAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeatherService extends Service implements WeatherInterface {
    private final String log_deug_tag = "WeatherService";
    private final String location = "101010100";
    private final String key = "d877ed5d19aa4c8ab209bf911c5fe561";

    private Looper serviceLooper;
    private WeatherServiceHandler weatherServiceHandler;

    private String raw_data;
    private Weather weather;
    private WeatherNow weatherNow;

    public WeatherService() {
    }

    /**
     * Handler用于应用startService启动服务时时使用
     */
    private final class WeatherServiceHandler extends Handler {
        public WeatherServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            stopSelf();
        }
    }

    private final WeatherBinder weatherBinder = new WeatherBinder();

    protected final class WeatherBinder extends Binder {
        public WeatherBinder() {
            super();
        }

        /**
         * 返回服务自身，实现应用服务间通信
         * @return
         */
        WeatherService getService() {
            return WeatherService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(log_deug_tag, "onBind start");
        return weatherBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        Message msg = weatherServiceHandler.obtainMessage();
        msg.arg1 = startId;
        weatherServiceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        serviceLooper = thread.getLooper();
        weatherServiceHandler = new WeatherServiceHandler(serviceLooper);
    }

    /**
     * 解析原始数据并存储到java bean中
     */
    public void get_raw_data() {
        new Thread(() -> {
            try {
                HttpURLConnection connection;
                String url_str = String.format("https://devapi.qweather.com/v7/weather/now?location=%s&key=%s", location, key);
                Log.d(log_deug_tag, url_str);
                URL url = new URL(url_str);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(500);
                connection.setConnectTimeout(500);
                if(connection.getResponseCode()==200){
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(inputStream));
                    raw_data = reader.readLine();
                    Log.d("WeatherService", raw_data);
                    reader.close();
                    inputStream.close();

                    /**
                     * 使用FastJson解析json数据
                     * weather: 顶层数据
                     * weatherNow: 内层now数据
                     */
                    weather = JSON.parseObject(raw_data, Weather.class);
                    Log.d(log_deug_tag, weather.getNow());
                    weatherNow = JSON.parseObject(weather.getNow(), WeatherNow.class);
                    Log.d(log_deug_tag, weatherNow.getWindDir());
                }

//                    Log.d(log_deug_tag, weatherNow.toStringList());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void parser_data(){
        get_raw_data();
        new Thread(()->{

        }).start();
    }

    /**
     * 返回通过新数据生成的Adapter
     * @param context
     * @return
     */
    public WeatherNowAdapter getWeatherNowAdapter(Context context) {
        Log.d(log_deug_tag, weatherNow.toString());
        String[] fixNames = {"数据观测时间",
                "温度", "体感温度", "图标的代码",
                "天气状况", "风向360角度", "风向",
                "风力等级", "风速km/h", "相对湿度",
                "当前小时累计降水量", "大气压强", "能见度",
                "云量", "露点温度"};
        List<String> fixNameList = Arrays.asList(fixNames);
        List<String> varTimeList = new ArrayList<>();
        for (int i = 0; i < fixNameList.size(); i++)
            varTimeList.add(getObsTime().split("T")[0]);
        List<String> varValueList = Arrays.asList(weatherNow.toStringList().split(","));
        if(weatherNow!=null)
            return new WeatherNowAdapter(context, fixNameList, varTimeList, varValueList);
        else
            return null;
    }

    @Override
    public String getCode() {
        return weather.getCode();
    }

    @Override
    public String getUpdateTime() {
        return weather.getUpdateTime();
    }

    @Override
    public String getFxLink() {
        return weather.getFxLink();
    }

    @Override
    public String getObsTime() {
        return weatherNow.getObstime();
    }

    @Override
    public String getTemp() {
        return weatherNow.getTemp();
    }

    @Override
    public String getFeelsLike() {
        return weatherNow.getFeelsLike();
    }

    @Override
    public String getIcon() {
        return weatherNow.getIcon();
    }

    @Override
    public String getText() {
        return weatherNow.getText();
    }

    @Override
    public String getWind360() {
        return weatherNow.getWind360();
    }

    @Override
    public String getWindDir() {
        return weatherNow.getWindDir();
    }

    @Override
    public String getWindScale() {
        return weatherNow.getWindScale();
    }

    @Override
    public String getWindSpeed() {
        return weatherNow.getWindSpeed();
    }

    @Override
    public String getHumidity() {
        return weatherNow.getHumidity();
    }

    @Override
    public String getPrecip() {
        return weatherNow.getPrecip();
    }

    @Override
    public String getPressure() {
        return weatherNow.getPressure();
    }

    @Override
    public String getVis() {
        return weatherNow.getVis();
    }

    @Override
    public String getCloud() {
        return weatherNow.getCloud();
    }

    @Override
    public String getDew() {
        return weatherNow.getDew();
    }

    public WeatherNow getWeatherNow() {
        return weatherNow;
    }

    public void setWeatherNow(WeatherNow weatherNow) {
        this.weatherNow = weatherNow;
    }
}