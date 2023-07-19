package com.liuyaoli.myapplication.weatherrecycler

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liuyaoli.myapplication.R

class HourlyReportViewHolder : RecyclerView.ViewHolder {
    var hourlyReport : RecyclerView
        private set
    constructor(view : View) : super(view){
        hourlyReport =view.findViewById(R.id.hourlyReport)
    }
}