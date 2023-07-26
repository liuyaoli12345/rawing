package com.liuyaoli.myapplication.homerecycler

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liuyaoli.myapplication.R

class PlaintTextViewHolder : RecyclerView.ViewHolder {
    var tvTitle : TextView
        private set
    var tvMsg : TextView
        private set
    var tvAuthor : TextView
    constructor(view : View) : super(view){
        tvTitle = view.findViewById(R.id.mainTitle)
        tvMsg = view.findViewById(R.id.Msg)
        tvAuthor = view.findViewById(R.id.Author)
    }
}