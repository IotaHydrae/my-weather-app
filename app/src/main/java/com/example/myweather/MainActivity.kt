package com.example.myweather

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var weatherBinder: WeatherService.WeatherBinder
    private lateinit var weatherService: WeatherService
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {

        /**
         * Activity与Service绑定成功的时候被调用
         */
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.d("MainActivity", "OnServiceConnected")
            weatherBinder = p1 as WeatherService.WeatherBinder
            weatherService = weatherBinder.service
            mBound = true

            weatherBinder.parse_raw_data()
        }

        /**
         * Service的创建进程崩溃或者被杀掉的时候才会调用
         */
        override fun onServiceDisconnected(p0: ComponentName?) {
            mBound = false
            Log.d("MainActivity", "OnServiceDisconnected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        /**
         * 设置导航栏按钮
         */
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        /**
         * 设置NavigationView监听事件
         */
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_menu_city_list -> {
                    Toast.makeText(this, "nav city list clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> true
            }
            drawerLayout.closeDrawers()
            true
        }

        main_fab.setOnClickListener {
            updateFrame()
            Toast.makeText(this, "数据已刷新", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        /**
         * 客户端通过调用 bindService() 绑定到服务。
         * bindService() 的返回值指示所请求的服务是否存在，以及是否允许客户端访问该服务。
         * 调用时，它必须提供 ServiceConnection 的实现，后者会监控与服务的连接。
         */
        Intent(this, WeatherService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }

    /**
     * 使用自定义菜单
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /**
     * 菜单点击事件
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home, R.id.menu_chose_city -> {
                Toast.makeText(this, "chose city clicked", Toast.LENGTH_SHORT).show()
                drawerLayout.openDrawer(GravityCompat.END)
                true
            }

            R.id.menu_share -> {
                Toast.makeText(this, "share clicked", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.menu_settings -> {
                Toast.makeText(this, "settings clicked", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateFrame() {
        if (mBound) {
            progress_view.visibility = View.VISIBLE
            weatherBinder.parse_raw_data()
            main_fab.setImageResource(R.drawable.ic_menu)
            edit_obsTime.let {
                it.setText(weatherService.getObsTime())
            }
            edit_temp.let {
                it.setText(weatherService.getTemp())
            }
            edit_feelsLike.let {
                it.setText(weatherService.getFeelsLike())
            }
            edit_text.let {
                it.setText(weatherService.getText())
            }
            edit_windDir.let {
                it.setText(weatherService.getWindDir())
            }
            edit_pressure.let {
                it.setText(weatherService.getPressure())
            }
            main_fab.setImageResource(R.drawable.ic_refresh)
            progress_view.visibility = View.INVISIBLE
        }
    }
}