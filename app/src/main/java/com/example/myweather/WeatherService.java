package com.example.myweather;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService extends Service {
    private String log_deug_tag = "WeatherService";

    public WeatherService() {
    }

    private WeatherBinder weatherBinder=null;

    protected final class WeatherBinder extends Binder{
        private HttpURLConnection connection = null;
        private String location = "101010100";
        private String key = "d877ed5d19aa4c8ab209bf911c5fe561";

        public WeatherBinder(){
            super();
        }

        public void get_raw_data(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        StringBuilder stringBuilder = new StringBuilder();
//                        https://free-api.heweather.net/s6/weather/now?location=101010100&key=d877ed5d19aa4c8ab209bf911c5fe561
//                        String bytes = String.format("https://devapi.qweather.com/v7/weather/24h?location=%s&key=%s",location,key);
                        String bytes = String.format("https://free-api.heweather.net/s6/weather/now?location=%s&key=%s",location,key);
                        Log.d(log_deug_tag, bytes);
                        URL url = new URL(bytes);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setReadTimeout(8000);
                        connection.setConnectTimeout(8000);
//                        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
//
//                        dataOutputStream.writeBytes(bytes);
                        InputStream inputStream = connection.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        Log.d("WeatherService", reader.readLine().toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        connection.disconnect();
                    }
                }
            }).start();
        }

        public void get_progress(){

        }

    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(log_deug_tag, "onBind start");
        weatherBinder = new WeatherBinder();
        return weatherBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}