package com.example.myweather.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R

class WeatherNowAdapter(val weatherList:List<String>): RecyclerView.Adapter<WeatherNowAdapter.ViewHolder>(){

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val weatherNowImage:ImageView = view.findViewById(R.id.weatherNowImage)
        val weatherNowText: TextView = view.findViewById(R.id.weatherNowText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_now_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weather = weatherList[position]
        holder.weatherNowText.text =
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}