package com.liuyaoli.myapplication.weatherrecycler.hourlyreportrecycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liuyaoli.myapplication.R

class HourlyReportItemViewHolder : RecyclerView.ViewHolder {
    var tvWeather : ImageView
        private set
    var tvTime : TextView
        private set
    var tvTemper : TextView
        private set
    constructor(view : View) : super(view){
        tvWeather = view.findViewById(R.id.cur_weather)
        tvTime = view.findViewById(R.id.cur_time)
        tvTemper = view.findViewById(R.id.cur_temper)
    }
}