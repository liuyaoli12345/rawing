package com.liuyaoli.myapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
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

class WeatherActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeatherAdapter
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_page)
        showRecyclerView()
        val backButton: ImageButton = findViewById(R.id.weatherPageBackButton)
        backButton.setOnClickListener {
            finish()
        }

        weatherViewModel = ViewModelLazy(WeatherViewModel::class, { viewModelStore }, {
            defaultViewModelProviderFactory
        }).value

        // Manually trigger the observer once in onCreate
       getWeatherData()

        weatherViewModel.weatherData.observe(this, Observer { weatherData ->
            updateUI(weatherData)
        })
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