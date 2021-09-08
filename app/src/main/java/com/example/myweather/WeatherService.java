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
import com.example.myweather.weather.WeatherInterface;
import com.example.myweather.weather.WeatherNow;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    public void parse_raw_data() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection;
                    String url_str = String.format("https://devapi.qweather.com/v7/weather/now?location=%s&key=%s", location, key);
                    Log.d(log_deug_tag, url_str);
                    URL url = new URL(url_str);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(500);
                    connection.setConnectTimeout(500);
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(inputStream));
                    raw_data = reader.readLine();
                    Log.d("WeatherService", raw_data);
                    reader.close();

                    /**
                     * 使用FastJson解析json数据
                     * weather: 顶层数据
                     * weatherNow: 内层now数据
                     */
                    weather = JSON.parseObject(raw_data, Weather.class);
                    Log.d(log_deug_tag, weather.getNow());
                    weatherNow = JSON.parseObject(weather.getNow(), WeatherNow.class);
                    Log.d(log_deug_tag, weatherNow.getWindDir());
                    Log.d(log_deug_tag, weatherNow.toStringList());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
//    public void get_raw_json() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    OkHttpClient okHttpClient = new OkHttpClient();
//                    String url_str = String.format("https://devapi.qweather.com/v7/weather/now?location=%s&key=%s", location, key);
//                    Request request = new Request.Builder()
//                            .url(url_str)
//                            .build();
//                    Response response = okHttpClient.newCall(request).execute();
//                    Log.d(log_deug_tag, response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//    }



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