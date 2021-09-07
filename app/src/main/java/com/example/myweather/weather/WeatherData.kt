package com.example.myweather.weather

data class WeatherData(
    val code: String,
    val updateTime: String,
    val fxLink: String
) {
    var obstime: String = "None"
    var temp: String = "None"
    var feelsLike: String = "None"
    var icon: String = "None"
    var text: String = "None"
    var wind360: String = "None"
    var windDir: String = "None"
    var windScale: String = "None"
    var windSpeed: String = "None"
    var humidity: String = "None"
    var precip: String = "None"
    var pressure: String = "None"
    var vis: String = "None"
    var cloud: String = "None"
    var dew: String = "None"
}
