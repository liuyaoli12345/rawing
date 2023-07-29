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
            VideoBean("https://files.lsmcloud.top/blog26111653219843_.jpg","练习了好久的后空翻，今天终于成功了！","胥子180",0),
            VideoBean("https://files.lsmcloud.top/blog202307291756099.png","狗子第一次学会撸猫，控制欲拉满","萌宠天地",1),
            VideoBean("https://files.lsmcloud.top/blog202307291809598.png","郭继承教授如来原版，令人匪夷所思！","鬼畜社区",2),
            VideoBean("https://files.lsmcloud.top/blog202307291812434.png","俄乌开战以来战线变化，一目了然！","俄乌局势",3),
            VideoBean("https://files.lsmcloud.top/blog202307291815173.png","【文件压缩极限挑战】我居然把华强买瓜压缩到了378KB","鬼畜天下",4),
            VideoBean("https://files.lsmcloud.top/blog202307291818484.png","暴风雨来袭！AI全面进军短视频行业！","科技评论",5),
            )
        adapter = VideoAdapter(items)
        recyclerView.adapter = adapter
    }

}