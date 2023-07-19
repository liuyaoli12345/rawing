package com.liuyaoli.myapplication.mainfragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liuyaoli.myapplication.R
import com.liuyaoli.myapplication.R.*
import com.liuyaoli.myapplication.WeatherActivity
import com.liuyaoli.myapplication.homerecycler.HomeAdapter
import com.liuyaoli.myapplication.homerecycler.ImgAndTextBean
import com.liuyaoli.myapplication.homerecycler.PlainTextBean

class HomeFragment : Fragment() {
    private var weatherTransparentAnim: ConstraintLayout?=null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    private lateinit var view: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(layout.home_page, container, false)
        showRecyclerView()
        showWeatherTransparent()
        val weatherLayout: ConstraintLayout = view.findViewById(R.id.weather)
        weatherLayout.setOnClickListener {
            // 在此处处理点击事件
            // 在这里执行页面跳转的代码
            val intent = Intent(this.context, WeatherActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    private fun showWeatherTransparent(){
        weatherTransparentAnim = view.findViewById(R.id.weather)

        val anim = AnimationUtils.loadAnimation(this.context, R.anim.weather_transparent_anim)
        anim.interpolator = LinearInterpolator()
        anim.repeatCount= Animation.INFINITE
        weatherTransparentAnim?.startAnimation(anim)
    }

    private fun showRecyclerView(){
        recyclerView = view.findViewById(R.id.homeRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        val items = listOf<Any>(
            PlainTextBean("商鞅：疑事无功，疑行无名","置顶","史记"),
            ImgAndTextBean(drawable.simaqian,"太后：令吾百岁之后，皆鱼肉之矣","热点","司马迁"),
            ImgAndTextBean(drawable.simaqian,"太后：令吾百岁之后，皆鱼肉之矣","热点","司马迁"),
            ImgAndTextBean(drawable.simaqian,"太后：令吾百岁之后，皆鱼肉之矣","热点","司马迁"),
            ImgAndTextBean(drawable.simaqian,"太后：令吾百岁之后，皆鱼肉之矣","热点","司马迁"),
            ImgAndTextBean(drawable.simaqian,"太后：令吾百岁之后，皆鱼肉之矣","热点","司马迁"),
            ImgAndTextBean(drawable.simaqian,"太后：令吾百岁之后，皆鱼肉之矣","热点","司马迁"),
            ImgAndTextBean(drawable.simaqian,"太后：令吾百岁之后，皆鱼肉之矣","热点","司马迁")
        )
        adapter = HomeAdapter(items)
        recyclerView.adapter = adapter
    }
}