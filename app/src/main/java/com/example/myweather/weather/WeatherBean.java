package com.example.myweather.weather;

public class WeatherBean {
    private WeatherEnums type;
    private String name;
    private String time;
    private int imgResId;
    private String value;

    public WeatherBean(WeatherEnums type, String name, String time, int imgResId, String value) {
        this.type = type;
        this.name = name;
        this.time = time;
        this.imgResId = imgResId;
        this.value = value;
    }

    public WeatherEnums getType() {
        return type;
    }

    public void setType(WeatherEnums type) {
        this.type = type;
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

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
