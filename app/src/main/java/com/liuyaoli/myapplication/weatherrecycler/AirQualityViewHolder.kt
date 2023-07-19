package com.liuyaoli.myapplication.weatherrecycler

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liuyaoli.myapplication.R

class AirQualityViewHolder : RecyclerView.ViewHolder {
    var tvAqi : TextView
        private set
    var tvLevel : TextView
        private set
    constructor(view : View) : super(view){
        tvAqi = view.findViewById(R.id.aqi)
        tvLevel = view.findViewById(R.id.air_level)
    }
}