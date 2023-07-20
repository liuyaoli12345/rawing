package com.liuyaoli.myapplication.videorecycler

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.liuyaoli.myapplication.R
import com.liuyaoli.myapplication.VideoStreamActivity


class VideoAdapter(private val items: List<VideoBean>) : RecyclerView.Adapter<VideoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
        val videoItem : ShapeableImageView = view.findViewById(R.id.video_item_cover)
        videoItem.setOnClickListener {
            val intent = Intent(parent.context, VideoStreamActivity :: class.java)
            parent.context.startActivity(intent)
        }
        return VideoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val item = items[position]
        val videoBean = item as VideoBean
        videoBean.cover_url?.let {
            holder.tvCover.setImageResource(it)
        }

        videoBean.title?.let {
            holder.tvTitle.text = it
        }

        videoBean.comment_num?.let {
            holder.tvComment.text = it.toString()
        }

        videoBean.profile_url?.let {
            holder.tvProfile.setCompoundDrawablesWithIntrinsicBounds(it,0,0,0)
        }

        videoBean.profile_name?.let {
            holder.tvProfile.text = it
        }
    }

}