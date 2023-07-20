package com.liuyaoli.myapplication.videorecycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liuyaoli.myapplication.R

class VideoViewHolder: RecyclerView.ViewHolder {
    var tvCover : ImageView
        private set
    var tvTitle : TextView
        private set
    var tvProfile : TextView
        private set
    var tvComment : TextView
        private set
    constructor(view : View) : super(view){
        tvCover = view.findViewById(R.id.video_item_cover)
        tvTitle = view.findViewById(R.id.video_item_title)
        tvComment = view.findViewById(R.id.video_item_comments)
        tvProfile = view.findViewById(R.id.video_item_profile)
    }
}