package com.liuyaoli.myapplication

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelLazy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.liuyaoli.myapplication.constants.PermissionConstants
import com.liuyaoli.myapplication.mvvm.model.WeatherData
import com.liuyaoli.myapplication.mvvm.viewmodel.WeatherViewModel
import com.liuyaoli.myapplication.weatherrecycler.AirQualityBean
import com.liuyaoli.myapplication.weatherrecycler.HourlyReportBean
import com.liuyaoli.myapplication.weatherrecycler.WarningBean
import com.liuyaoli.myapplication.weatherrecycler.WeatherAdapter
import com.liuyaoli.myapplication.weatherrecycler.hourlyreportrecycler.HourlyReportItemBean
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WeatherActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeatherAdapter
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherLoadingAvi: AVLoadingIndicatorView
    private lateinit var weatherLoadingBackground: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_page)
        weatherLoadingBackground = findViewById(R.id.weather_page_loading_background)
        val anim = AnimationUtils.loadAnimation(this, R.anim.weather_loading_transparent_anim)
        anim.interpolator = LinearInterpolator()
        val animationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // 动画开始时执行的操作（可选）
            }
            override fun onAnimationEnd(animation: Animation?) {
                // 动画结束时执行的操作
                weatherLoadingBackground.visibility = View.GONE // 隐藏View
            }
            override fun onAnimationRepeat(animation: Animation?) {
                // 动画重复时执行的操作（可选）
            }
        }
        anim.setAnimationListener(animationListener)
        object : Thread(){
            override fun run() {
                weatherLoadingAvi = findViewById(R.id.weather_page_avi)
                runOnUiThread {
                    weatherLoadingAvi.show()
                }
                sleep(3000)
                runOnUiThread {
                    weatherLoadingAvi.hide()
                    weatherLoadingBackground.startAnimation(anim)
                }
                super.run()
            }
        }.start()
        showRecyclerView()
        val backButton: ImageButton = findViewById(R.id.weatherPageBackButton)
        backButton.setOnClickListener {
            finish()
        }

        showWeatherTransparent()

        weatherViewModel = ViewModelLazy(WeatherViewModel::class, { viewModelStore }, {
            defaultViewModelProviderFactory
        }).value

        // Manually trigger the observer once in onCreate
       getWeatherData()

        weatherViewModel.weatherData.observe(this, Observer { weatherData ->
            updateUI(weatherData)
        })
    }

    override fun onDestroy() {
        recyclerView.adapter = null
        super.onDestroy()
    }

    private fun updateUI(weatherData: WeatherData){
        val temperatureTextView = findViewById<TextView>(R.id.weather_page_temp)
        temperatureTextView.text= "${weatherData.temperature}˚"
        val weatherDescriptionTextView = findViewById<TextView>(R.id.weather_page_des)
        weatherDescriptionTextView.text = weatherData.weatherDescription
        val weatherMaxMinTextView = findViewById<TextView>(R.id.weather_page_max_min)
        weatherMaxMinTextView.text = "最高${weatherData.maxTemperature}˚ 最低${weatherData.minTemperature}˚"
        val weatherPlaceTextView = findViewById<TextView>(R.id.weather_page_place)
        weatherPlaceTextView.text = "经度${weatherData.longitude} 纬度${weatherData.latitude}"
    }

    private fun showRecyclerView(){
        val hourlyReportItems = listOf<HourlyReportItemBean>(
            HourlyReportItemBean(R.drawable.storm, "现在", "26˚"),
            HourlyReportItemBean(R.drawable.rainy, "13时", "24˚"),
            HourlyReportItemBean(R.drawable.cloudy, "14时", "23˚"),
            HourlyReportItemBean(R.drawable.sunny,"15时","28˚"),
            HourlyReportItemBean(R.drawable.foggy, "16时", "26˚")
        )
        recyclerView = findViewById(R.id.weatherRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val items = listOf<Any>(
            WarningBean("雷暴","雷暴，明日19时结束","GAGA预警中心"),
            WarningBean("台风","台风，明日19时结束","GAGA预警中心"),
            AirQualityBean("20","20-优"),
            HourlyReportBean(hourlyReportItems)
        )
        adapter = WeatherAdapter(items)
        recyclerView.adapter = adapter
    }

    private var queryLocationAgain = false

    private fun showWeatherTransparent() {
        val weatherTransparentAnim = findViewById<TextView>(R.id.weather_page_temp)

        val anim = AnimationUtils.loadAnimation(this, R.anim.weather_transparent_anim)
        anim.interpolator = LinearInterpolator()
        anim.repeatCount = Animation.INFINITE
        weatherTransparentAnim?.startAnimation(anim)
    }
    private fun getWeatherData() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.i("qwerty", "enter getWeatherData()")
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted, get the user's location
            val locationRequest = LocationRequest().setInterval(200000).setFastestInterval(200000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {
                            weatherViewModel.getWeatherData(location.latitude, location.longitude)
                            Log.i("qwerty", "Try get weather data")
                        }
                        fusedLocationClient.removeLocationUpdates(this)
                    }
                },
                Looper.myLooper()
            )
        } else if(!queryLocationAgain){
            // Request location permission
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PermissionConstants.LOCATION_PERMISSION_CODE)
            getWeatherData()
        }
    }
}