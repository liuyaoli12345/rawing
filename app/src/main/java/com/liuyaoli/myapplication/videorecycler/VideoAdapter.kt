package com.liuyaoli.myapplication.videorecycler

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.liuyaoli.myapplication.MyApplication
import com.liuyaoli.myapplication.MyApplication.Companion.context
import com.liuyaoli.myapplication.R
import com.liuyaoli.myapplication.VideoStreamActivity


class VideoAdapter(private val items: List<VideoBean>) : RecyclerView.Adapter<VideoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
        val videoItem : ShapeableImageView = view.findViewById(R.id.video_item_cover)
        val likeButton = view.findViewById<ImageButton>(R.id.video_item_like)
        likeButton.setOnClickListener {
            val originalDrawable = likeButton.drawable
            val newColor = parent.context.resources.getColor(R.color.red, null)
            val colorFilter = PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN)
            originalDrawable.colorFilter = colorFilter
            likeButton.setImageDrawable(originalDrawable)
        }
        val commentButton = view.findViewById<ImageButton>(R.id.video_item_comments)
        commentButton.setOnClickListener {
            val originalDrawable = commentButton.drawable
            val newColor = parent.context.resources.getColor(R.color.orange, null)
            val colorFilter = PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN)
            originalDrawable.colorFilter = colorFilter
            commentButton.setImageDrawable(originalDrawable)
        }
        val followButton = view.findViewById<ImageButton>(R.id.video_item_follow)
        followButton.setOnClickListener {
            followButton.setImageResource(R.drawable.followed_button)
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
            Glide.with(MyApplication.context)
                .load(it)
                .placeholder(R.drawable.place_holder) // 设置占位图
                .error(R.drawable.network_err)
                .timeout(6000)// 设置加载错误时显示的图片
                .into(holder.tvCover)
        }

        videoBean.title?.let {
            holder.tvTitle.text = it
        }


        videoBean.profile_name?.let {
            holder.tvProfile.text = it
        }
        Log.i("kkkkkk","添加Listener！")
        holder.tvCover.setOnClickListener {
            Log.i("kkkkkk","被点击了")
            val bundle = Bundle()
            bundle.putInt("videoId", videoBean.videoIdx)
            val intent = Intent(context, VideoStreamActivity :: class.java)
            intent.putExtras(bundle)
            holder.itemView.context.startActivity(intent)
        }
    }

}