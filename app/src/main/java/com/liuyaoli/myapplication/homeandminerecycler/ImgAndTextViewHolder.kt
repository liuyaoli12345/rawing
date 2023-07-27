package com.liuyaoli.myapplication.homeandminerecycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liuyaoli.myapplication.R

class ImgAndTextViewHolder : RecyclerView.ViewHolder {
    var ivCover : ImageView
        private set
    var tvTitle : TextView
        private set
    var tvMsg : TextView
        private set
    var tvAuthor : TextView
    constructor(view : View) : super(view){
        ivCover = view.findViewById(R.id.news_cover)
        tvTitle = view.findViewById(R.id.mainTitle)
        tvMsg = view.findViewById(R.id.Msg)
        tvAuthor = view.findViewById(R.id.Author)
    }
}