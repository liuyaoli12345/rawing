package com.liuyaoli.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liuyaoli.myapplication.weatherrecycler.AirQualityBean
import com.liuyaoli.myapplication.weatherrecycler.HourlyReportBean
import com.liuyaoli.myapplication.weatherrecycler.WarningBean
import com.liuyaoli.myapplication.weatherrecycler.WeatherAdapter
import com.liuyaoli.myapplication.weatherrecycler.hourlyreportrecycler.HourlyReportItemBean

class WeatherActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeatherAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_page)
        showRecyclerView()
        val backButton: ImageButton = findViewById(R.id.weatherPageBackButton)
        backButton.setOnClickListener {
            finish()
        }
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
}