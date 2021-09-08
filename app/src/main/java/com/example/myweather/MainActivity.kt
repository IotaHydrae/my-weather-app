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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var weatherBinder: WeatherService.WeatherBinder
    private lateinit var weatherService: WeatherService
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {

        /**
         * Activity与Service绑定成功的时候被调用
         */
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            Log.d("MainActivity", "OnServiceConnected")
            weatherBinder = service as WeatherService.WeatherBinder
            weatherService = weatherBinder.service
            mBound = true

            weatherService.parse_raw_data()
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

        /**
         * 设置悬浮按钮点击事件
         */
        main_fab.setOnClickListener { view ->
            var status: Boolean

            Snackbar.make(view, "是否刷新数据？", Snackbar.LENGTH_SHORT)
                .setAction("是") {
//                    status = updateWeatherDisplay()
//
//                    if (status)
//                        Toast.makeText(this, "数据已刷新", Toast.LENGTH_SHORT).show()
//                    else
//                        Toast.makeText(this, "数据刷新失败！", Toast.LENGTH_SHORT).show()
                }
                .show()


        }
        /**
         * 设置Tablayout属性
         */


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

//    private fun updateWeatherDisplay(): Boolean {
//        if (mBound) {
//            progress_view.visibility = View.VISIBLE
//            weatherService.parse_raw_data()
//            main_fab.setImageResource(R.drawable.ic_menu)
//            try {
//                weatherService.getObsTime()?.let {
//                    edit_obsTime.setText(it)
//                }
//                weatherService.getTemp()?.let {
//                    edit_temp.setText(it)
//                }
//                weatherService.getFeelsLike()?.let {
//                    edit_feelsLike.setText(it)
//                }
//                weatherService.getText().let {
//                    edit_text.setText(it)
//                }
//                weatherService.getWindDir()?.let {
//                    edit_windDir.setText(it)
//                }
//                weatherService.getPressure()?.let {
//                    edit_pressure.setText(it)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                return false
//            } finally {
//                main_fab.setImageResource(R.drawable.ic_refresh)
//                progress_view.visibility = View.INVISIBLE
//            }
//        } else {
//            return false
//        }
//        return true
//    }
}