package com.liuyaoli.myapplication.homeandminerecycler

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liuyaoli.myapplication.NewsContentActivity
import com.liuyaoli.myapplication.R

class HomeAndMineAdapter(private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_PLAINTEXT = 0
        private const val VIEW_TYPE_IMG_AND_TEXT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is PlainTextBean -> VIEW_TYPE_PLAINTEXT
            is ImgAndTextBean -> VIEW_TYPE_IMG_AND_TEXT
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_PLAINTEXT -> {
                val view = inflater.inflate(R.layout.news_plain_text_item, parent, false)
                PlaintTextViewHolder(view)
            }
            VIEW_TYPE_IMG_AND_TEXT -> {
                val view = inflater.inflate(R.layout.news_image_text_item, parent, false)
                ImgAndTextViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is PlaintTextViewHolder -> {
                val plainTextBean = item as PlainTextBean
//                plainTextBean.coverUrl?.let {
//                    holder.ivCover?.setImageResource(it)
//                }

                plainTextBean.title?.let {
                    holder.tvTitle.text = it
                }

                plainTextBean.status?.let {
                    holder.tvMsg.text = it
                }

                plainTextBean.author?.let {
                    holder.tvAuthor.text = it
                }
                holder.itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putLong("id",plainTextBean.id)
                    bundle.putString("title", plainTextBean.title)
                    bundle.putString("author", plainTextBean.author)
                    val intent = Intent(holder.itemView.context, NewsContentActivity::class.java)
                    intent.putExtras(bundle)
                    holder.itemView.context.startActivity(intent)
                }
            }
            is ImgAndTextViewHolder -> {
                val imgAndTextBean = item as ImgAndTextBean
                imgAndTextBean.coverUrl?.let {
                    holder.ivCover?.setImageResource(it)
                }

                imgAndTextBean.title?.let {
                    holder.tvTitle.text = it
                }

                imgAndTextBean.status?.let {
                    holder.tvMsg.text = it
                }

                imgAndTextBean.author?.let {
                    holder.tvAuthor.text = it
                }
                holder.itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putLong("id",imgAndTextBean.id)
                    bundle.putString("title", imgAndTextBean.title)
                    bundle.putString("author", imgAndTextBean.author)
                    val intent = Intent(holder.itemView.context, NewsContentActivity::class.java)
                    intent.putExtras(bundle)
                    holder.itemView.context.startActivity(intent)
                }
            }
            }
        }
    override fun getItemCount(): Int {
        return items.size
    }
}
