package com.liuyaoli.myapplication.weatherrecycler.hourlyreportrecycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liuyaoli.myapplication.R

class HourlyReportItemAdapter(private val items: List<HourlyReportItemBean>) : RecyclerView.Adapter<HourlyReportItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyReportItemViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.hourly_weather_report_item, parent, false)
        return HourlyReportItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyReportItemViewHolder, position: Int) {
        val hourlyReportItemBean = items[position]

        hourlyReportItemBean.cur_weather?.let {
            holder.tvWeather.setImageResource(it)
        }

        hourlyReportItemBean.cur_temper?.let {
            holder.tvTemper.text = it
        }

        hourlyReportItemBean.cur_time?.let {
            holder.tvTime.text = it
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
}