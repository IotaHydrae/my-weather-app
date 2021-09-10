package com.example.myweather;

import android.app.Service;
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
import com.example.myweather.weather.WeatherBean;
import com.example.myweather.weather.WeatherEnums;
import com.example.myweather.weather.WeatherInterface;
import com.example.myweather.weather.WeatherNow;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
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
        super.onDestroy();
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
                connection.setReadTimeout(300);
                connection.setConnectTimeout(300);
                if (connection.getResponseCode() == 200) {
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
//
//                    Map weatherMap = JSON.parseObject(raw_data,Map.class);
//                    for (Object obj : weatherMap.keySet()){
//                        Log.d("main", "key:"+obj+", value: "+weatherMap.keySet());
//                    }

//                    Map weatherNowMap = JSON.parseObject(weather.getNow(), Map.class);
//                    for (Object obj : weatherNowMap.keySet()){
//                        Log.d("main", "**** key:"+obj+", value: "+weatherNowMap.get(obj));
//                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    public String getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        return new StringBuffer()
                .append(calendar.get(Calendar.MONTH))
                .append("/")
                .append(calendar.get(Calendar.DATE))
                .toString();
    }

    public String getWeatherTime() {
        if(weatherNow!=null){
            String[] str = getObsTime().split("T")[0].split("-");
            return new StringBuffer()/*.append(str[0]+"/")*/
                    .append(str[1] + "/")
                    .append(str[2])
                    .toString();
        }else
            return "";
    }

    public void initWeatherData(List<WeatherBean> weatherBeanList) {
        weatherBeanList.clear();
        String[] fixNames = {"数据观测时间",
                "温度", "体感温度", "图标的代码",
                "天气状况", "风向360角度", "风向",
                "风力等级", "风速km/h", "相对湿度",
                "当前小时累计降水量", "大气压强", "能见度",
                "云量", "露点温度"};
        List<String> fixNameList = Arrays.asList(fixNames);
        List<String> varTimeList = new ArrayList<>();
        if(weatherNow!=null) {

            for (int i = 0; i < fixNameList.size(); i++)
//            varTimeList.add(getObsTime().split("T")[0]);
                varTimeList.add(getWeatherTime());
            List<String> varValueList = weatherNow.toStringList();

            WeatherEnums type = WeatherEnums.RECYCLERVIEW_TYPE_HALF;
            for (int i = 0; i < fixNameList.size(); i++){
                if(i == 0){type=WeatherEnums.RECYCLERVIEW_TYPE_FULL;}
                else if(i>0&&i<5){type=WeatherEnums.RECYCLERVIEW_TYPE_HALF;}
                else {type=WeatherEnums.RECYCLERVIEW_TYPE_QUARTER;}
                weatherBeanList.add(new WeatherBean(
                        type,
                        fixNameList.get(i),
                        varTimeList.get(i),
                        0,
                        varValueList.get(i)
                ));
            }

        }else{
            for (int i = 0; i < fixNameList.size(); i++)
//            varTimeList.add(getObsTime().split("T")[0]);
                varTimeList.add(getTime());

            WeatherEnums type = WeatherEnums.RECYCLERVIEW_TYPE_HALF;
            for (int i = 0; i < fixNameList.size(); i++){
                if(i == 0){type=WeatherEnums.RECYCLERVIEW_TYPE_FULL;}
                else if(i>10){type=WeatherEnums.RECYCLERVIEW_TYPE_QUARTER;}
                weatherBeanList.add(new WeatherBean(
                        type,
                        fixNameList.get(i),
                        varTimeList.get(i),
                        0,
                        ""
                ));
            }

        }
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