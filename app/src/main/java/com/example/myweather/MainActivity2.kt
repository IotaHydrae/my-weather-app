package com.example.myweather

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.qweather.plugin.view.HeContent
import com.qweather.plugin.view.QWeatherConfig
import kotlinx.android.synthetic.main.activity_main2.*
import com.qweather.plugin.view.SuspendView




class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        QWeatherConfig.init("9e90bcf25d784bea86f231c31abc793b")

        //取消默认背景
        horizon_view.setDefaultBack(false)
        //设置布局的背景圆角角度，颜色，边框宽度，边框颜色
        horizon_view.setStroke(5, Color.BLUE,1,Color.BLUE)
        //添加地址文字描述，第一个参数为文字大小，单位：sp ，第二个参数为文字颜色，默认白色
        horizon_view.addLocation(14, Color.WHITE)
        //添加预警图标，参数为图标大小，单位：dp
        horizon_view.addAlarmIcon(14)
        //添加预警文字
        horizon_view.addAlarmTxt(14)
        //添加温度描述
        horizon_view.addTemp(14, Color.WHITE)
        //添加天气图标
        horizon_view.addWeatherIcon(14)
        //添加天气描述
        horizon_view.addCond(14, Color.WHITE)
        //添加风向图标
        horizon_view.addWindIcon(14)
        //添加风力描述
        horizon_view.addWind(14, Color.WHITE)
        //添加文字：AQI
        horizon_view.addAqiText(14, Color.WHITE)
        //添加空气质量描述
        horizon_view.addAqiQlty(14)
        //添加空气质量数值描述
        horizon_view.addAqiNum(14)
        //添加降雨图标
        horizon_view.addRainIcon(14)
        //添加降雨描述
        horizon_view.addRainDetail(14, Color.WHITE)
        //设置控件的对齐方式，默认居中
        horizon_view.setViewGravity(HeContent.GRAVITY_CENTER)
        //设置控件的内边距，默认为0
        horizon_view.setViewPadding(10,10,10,10)
        //显示控件
        horizon_view.show()

        val leftLayout = llView.leftLayout
        val rightTopLayout = llView.rightTopLayout
        val rightBottomLayout = llView.rightBottomLayout
        llView.setStroke(5, Color.parseColor("#313a44"), 1, Color.BLACK);

//添加温度描述到左侧大布局
//第一个参数为需要加入的布局
//第二个参数为文字大小，单位：sp
//第三个参数为文字颜色，默认白色
        llView.addTemp(leftLayout, 40, Color.WHITE);
//添加温度图标到右上布局，第二个参数为图标宽高（宽高1：1，单位：dp）
        llView.addWeatherIcon(rightTopLayout, 14);
//添加预警图标到右上布局
        llView.addAlarmIcon(rightTopLayout, 14);
//添加预警描述到右上布局
        llView.addAlarmTxt(rightTopLayout, 14);
//添加文字AQI到右上布局
        llView.addAqiText(rightTopLayout, 14);
//添加空气质量到右上布局
        llView.addAqiQlty(rightTopLayout, 14);
//添加空气质量数值到右上布局
        llView.addAqiNum(rightTopLayout, 14);
//添加地址信息到右上布局
        llView.addLocation(rightTopLayout, 14, Color.WHITE);
//添加天气描述到右下布局
        llView.addCond(rightBottomLayout, 14, Color.WHITE);
//添加风向图标到右下布局
        llView.addWindIcon(rightBottomLayout, 14);
//添加风力描述到右下布局
        llView.addWind(rightBottomLayout, 14, Color.WHITE);
//添加降雨图标到右下布局
        llView.addRainIcon(rightBottomLayout, 14);
//添加降雨描述到右下布局
        llView.addRainDetail(rightBottomLayout, 14, Color.WHITE);
//设置控件的对齐方式，默认居中
        llView.setViewGravity(HeContent.GRAVITY_LEFT);
//显示布局
        llView.show();

        val suspendView = SuspendView(this)
        //显示悬浮控件
        //显示悬浮控件
        suspendView.show()

        verticalView.setDefaultBack(false);
//方法参数同（6）横向布局
        verticalView.addLocation(14, Color.WHITE);
        verticalView.addTemp(14, Color.WHITE);
        verticalView.addWeatherIcon(14);
        verticalView.addCond(14, Color.WHITE);
        verticalView.addWindIcon(14);
        verticalView.addWind(14, Color.WHITE);
        verticalView.addAqiText(14, Color.WHITE);
        verticalView.addAqiQlty(14);
        verticalView.addAqiNum(14);
        verticalView.addAlarmIcon(14);
        verticalView.addAlarmTxt(14);
        verticalView.addRainIcon(14);
        verticalView.addRainDetail(14, Color.WHITE);
        verticalView.show();
    }
}