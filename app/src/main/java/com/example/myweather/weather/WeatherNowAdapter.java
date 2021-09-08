package com.example.myweather.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweather.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeatherNowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<String> weatherName;
    private List<String> weatherTime;
    private List<String> weatherValue;


    public WeatherNowAdapter(Context context,
                             List<String> name,
                             List<String> time,
                             List<String> value){
        mContext = context;
        weatherName = name;
        weatherTime = time;
        weatherValue = value;
    }

    private final class WeatherNowHolder extends RecyclerView.ViewHolder{
        public TextView weatherNowName;
        public TextView weatherNowTime;
        public TextView weatherNowValue;

        public WeatherNowHolder(@NonNull View itemView) {
            super(itemView);

            weatherNowName = (TextView) itemView.findViewById(R.id.weatherNowName);
            weatherNowTime = (TextView) itemView.findViewById(R.id.weatherNowTime);
            weatherNowValue = (TextView) itemView.findViewById(R.id.weatherNowValue);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.weather_now_item, parent, false);
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        return new WeatherNowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WeatherNowHolder weatherNowHolder = (WeatherNowHolder) holder;

        try{
            if(weatherName!=null)
            weatherNowHolder.weatherNowName.setText(weatherName.get(position));
            if(weatherTime!=null)
            weatherNowHolder.weatherNowTime.setText(weatherTime.get(position));
            if(weatherValue!=null)
            weatherNowHolder.weatherNowValue.setText(weatherValue.get(position));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return weatherName==null?0:weatherName.size();
    }


}
