package com.universe.myweather.weather

import com.universe.myweather.BasicInterface

interface WeatherInterface : BasicInterface {
    /* 数据观测时间 */
    fun getObsTime(): String?

    /* 	温度，默认单位：摄氏度 */
    fun getTemp(): String?

    /* 体感温度，默认单位：摄氏度 */
    fun getFeelsLike(): String?

    /* 天气状况和图标的代码，图标可通过天气状况和图标下载 */
    fun getIcon(): String?

    /* 天气状况的文字描述，包括阴晴雨雪等天气状态的描述 */
    fun getText(): String?


    fun getWind360(): String?    /* 风向360角度 */
    fun getWindDir(): String?    /*风向                                  */
    fun getWindScale(): String?    /*风力等级                              */
    fun getWindSpeed(): String?    /*风速，公里/小时                       */
    fun getHumidity(): String?    /*相对湿度，百分比数值                  */
    fun getPrecip(): String?    /*当前小时累计降水量，默认单位：毫米    */
    fun getPressure(): String?    /*大气压强，默认单位：百帕              */
    fun getVis(): String?    /*能见度，默认单位：公里                */
    fun getCloud(): String?    /*云量，百分比数值                      */
    fun getDew(): String?    /*露点温度                              */
}