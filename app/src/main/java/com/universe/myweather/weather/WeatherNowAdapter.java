package com.universe.myweather.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.universe.myweather.R;

import java.util.List;

public class WeatherNowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;

    private final List<WeatherBean> weatherBeanList;


    public WeatherNowAdapter(Context context, List<WeatherBean> weatherBeans) {
        mContext = context;
        weatherBeanList = weatherBeans;
    }

    private final class WeatherNowHolder extends RecyclerView.ViewHolder {
        public TextView weatherNowName;
        public TextView weatherNowTime;
        public TextView weatherNowValue;

        public WeatherNowHolder(@NonNull View itemView) {
            super(itemView);

            weatherNowName = itemView.findViewById(R.id.weatherNowName);
            weatherNowTime = itemView.findViewById(R.id.weatherNowTime);
            weatherNowValue = itemView.findViewById(R.id.weatherNowValue);
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

        try {
            if (weatherBeanList != null) {
                weatherNowHolder.weatherNowName.setText(weatherBeanList.get(position).getName());
                weatherNowHolder.weatherNowTime.setText(weatherBeanList.get(position).getTime());
                weatherNowHolder.weatherNowValue.setText(weatherBeanList.get(position).getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return weatherBeanList == null ? 0 : weatherBeanList.size();
    }


}
