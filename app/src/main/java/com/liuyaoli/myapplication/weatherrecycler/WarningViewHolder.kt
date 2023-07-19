package com.liuyaoli.myapplication.weatherrecycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liuyaoli.myapplication.R

class WarningViewHolder: RecyclerView.ViewHolder {
    var ivWarn : TextView
        private set
    var tvContent : TextView
        private set
    var tvSource : TextView
        private set
    constructor(view : View) : super(view){
        ivWarn = view.findViewById(R.id.warning)
        tvContent = view.findViewById(R.id.warning_content)
        tvSource = view.findViewById(R.id.warning_source)
    }
}