package com.liuyaoli.myapplication.mainfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liuyaoli.myapplication.R
import com.liuyaoli.myapplication.videorecycler.VideoAdapter
import com.liuyaoli.myapplication.videorecycler.VideoBean

class VideoFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VideoAdapter
    private lateinit var view: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.video_page, container, false)
        showRecyclerView()
        return view
    }

    private fun showRecyclerView(){
        recyclerView = view.findViewById(R.id.video_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        val items = listOf<VideoBean> (
            VideoBean(R.drawable.connan,"《名侦探柯南》最新剧集引争议",R.drawable.connan_profile,"动漫评论",999),
            VideoBean(R.drawable.basketball_boy,"南京高校篮球联赛今日开启",R.drawable.connan_profile,"南京日报",62),
            VideoBean(R.drawable.connan,"《名侦探柯南》最新剧集引争议",R.drawable.connan_profile,"动漫评论",999),
            VideoBean(R.drawable.basketball_boy,"南京高校篮球联赛今日开启",R.drawable.connan_profile,"南京日报",62),
            VideoBean(R.drawable.connan,"《名侦探柯南》最新剧集引争议",R.drawable.connan_profile,"动漫评论",999),
            VideoBean(R.drawable.basketball_boy,"南京高校篮球联赛今日开启",R.drawable.connan_profile,"南京日报",62),
            VideoBean(R.drawable.connan,"《名侦探柯南》最新剧集引争议",R.drawable.connan_profile,"动漫评论",999),
            VideoBean(R.drawable.basketball_boy,"南京高校篮球联赛今日开启",R.drawable.connan_profile,"南京日报",62),
            )
        adapter = VideoAdapter(items)
        recyclerView.adapter = adapter
    }

}