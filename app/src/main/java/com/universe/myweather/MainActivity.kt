package com.universe.myweather

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.universe.myweather.weather.WeatherBean
import com.universe.myweather.weather.WeatherEnums
import com.universe.myweather.weather.WeatherNowAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var weatherAdapter: WeatherNowAdapter
    private lateinit var weatherBinder: WeatherService.WeatherBinder
    private lateinit var weatherService: WeatherService
    private var mBound: Boolean = false

    private val weatherBeanList = ArrayList<WeatherBean>()

    private val connection = object : ServiceConnection {

        /**
         * Activity与Service绑定成功的时候被调用
         */
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            Log.d("MainActivity", "OnServiceConnected")
            weatherBinder = service as WeatherService.WeatherBinder
            weatherService = weatherBinder.service
            mBound = true
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

        initViews()
//        val MainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    /**
     * 初始化界面以及监听事件等
     */
    private fun initViews() {
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
                }
                R.id.nav_menu_exit -> {
                    Toast.makeText(this, "nav exit clicked", Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        /**
         * 设置Tablayout属性
         */

        /**
         * 设置RecycleView
         */
        Log.d("Main", "Setting RecycleView")
        main_recyclerView.postDelayed({ weatherService.initWeatherData(weatherBeanList) }, 1000)

        val gridLayoutManager = GridLayoutManager(this, 4)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val bean = weatherBeanList.get(position)
                return when (bean.type) {
                    WeatherEnums.RECYCLERVIEW_TYPE_FULL ->
                        return 4
                    WeatherEnums.RECYCLERVIEW_TYPE_HALF ->
                        return 2
                    WeatherEnums.RECYCLERVIEW_TYPE_QUARTER ->
                        return 1
                    else -> return 1
                }
            }
        }
        main_recyclerView.layoutManager = gridLayoutManager

        weatherAdapter = WeatherNowAdapter(this, weatherBeanList)
        main_recyclerView.adapter = weatherAdapter

        /**
         *  设置下拉刷新
         */
        Log.d("Main", "Setting swipeRefreshLayout")
        main_swipeRefreshLayout.setOnRefreshListener {
            refreshWeather(weatherAdapter)
        }

        /**
         * 设置悬浮按钮点击事件
         */
        main_fab.setOnClickListener { view ->
            main_swipeRefreshLayout.isRefreshing = true
            refreshWeather(weatherAdapter)
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
        /**
         * 实现onStart周期自动刷新recyclerView
         */
        main_swipeRefreshLayout.isRefreshing = true
        main_swipeRefreshLayout.postDelayed({ refreshWeather(weatherAdapter) }, 1000)
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

    /**
     * 在子线程中通过服务类方法Http请求数据，
     * 延时是为了让缓慢地Http请求获得地数据，
     * 能够跟得上Ui线程刷新速度，此外，
     * 应尽量减少Ui线程工作量。
     */
    private fun refreshWeather(adapter: WeatherNowAdapter) {
        thread {
            main_swipeRefreshLayout.post { weatherService.get_raw_data() }
            Thread.sleep(1000)
            weatherService.initWeatherData(weatherBeanList)
            runOnUiThread {
                adapter.notifyDataSetChanged()
                main_swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}