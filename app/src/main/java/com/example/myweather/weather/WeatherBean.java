package com.example.myweather.weather;

public class WeatherBean {
    private String name;
    private String time;
    private String value;

    public WeatherBean(String name, String time, String value) {
        this.name = name;
        this.time = time;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
