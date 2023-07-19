package com.liuyaoli.myapplication.weatherrecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liuyaoli.myapplication.R
import com.liuyaoli.myapplication.weatherrecycler.hourlyreportrecycler.HourlyReportItemAdapter

class WeatherAdapter(private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_WARNING = 0
        private const val VIEW_TYPE_AIR_QUALITY = 1
        private const val VIEW_TYPE_HOURLY_REPORT = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is WarningBean -> VIEW_TYPE_WARNING
            is AirQualityBean -> VIEW_TYPE_AIR_QUALITY
            is HourlyReportBean -> VIEW_TYPE_HOURLY_REPORT
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_WARNING -> {
                val view = inflater.inflate(R.layout.weather_warn_item, parent, false)
                WarningViewHolder(view)
            }
            VIEW_TYPE_AIR_QUALITY -> {
                val view = inflater.inflate(R.layout.air_quality_item, parent, false)
                AirQualityViewHolder(view)
            }
            VIEW_TYPE_HOURLY_REPORT -> {
                val view = inflater.inflate(R.layout.hourly_weather_report, parent, false)
                HourlyReportViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is WarningViewHolder -> {
                val warningBean = item as WarningBean
//                plainTextBean.coverUrl?.let {
//                    holder.ivCover?.setImageResource(it)
//                }

                warningBean.warn_type?.let {
                    holder.ivWarn.text = it
                }

                warningBean.warn_content?.let {
                    holder.tvContent.text = it
                }

                warningBean.warn_source?.let {
                    holder.tvSource.text = it
                }
            }
            is AirQualityViewHolder -> {
                val airQualityBean = item as AirQualityBean
                airQualityBean.aqi?.let {
                    holder.tvAqi.text = "当前AQI(CN)为" + it
                }

                airQualityBean.level?.let {
                    holder.tvLevel.text = it
                }

            }
            is HourlyReportViewHolder -> {
                val hourlyReportBean = item as HourlyReportBean
                holder.hourlyReport.layoutManager = LinearLayoutManager(holder.itemView.context,RecyclerView.HORIZONTAL,false)
                holder.hourlyReport.adapter = HourlyReportItemAdapter(hourlyReportBean.items)
            }
    
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
}
