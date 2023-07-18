package com.liuyaoli.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liuyaoli.myapplication.homerecycler.HomeAdapter
import com.liuyaoli.myapplication.homerecycler.ImgAndTextBean
import com.liuyaoli.myapplication.homerecycler.PlainTextBean

class HomeActivity : AppCompatActivity(){
    private var weatherTransparentAnim:ConstraintLayout?=null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)
        showWeatherTransparent()
        showRecyclerView()
        val weatherLayout: ConstraintLayout = findViewById(R.id.weather)
        weatherLayout.setOnClickListener {
            // 在此处处理点击事件
            // 在这里执行页面跳转的代码
            val intent = Intent(this, WeatherActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showWeatherTransparent(){
        weatherTransparentAnim = findViewById(R.id.weather)

        val anim = AnimationUtils.loadAnimation(this, R.anim.weather_transparent_anim)
        anim.interpolator = LinearInterpolator()
        anim.repeatCount=Animation.INFINITE
        weatherTransparentAnim?.startAnimation(anim)
    }

    private fun showRecyclerView(){
        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val items = listOf<Any>(
            PlainTextBean("商鞅：疑事无功，疑行无名","置顶","史记"),
            ImgAndTextBean(R.drawable.simaqian,"太后：令吾百岁之后，皆鱼肉之矣","热点","司马迁"),
            ImgAndTextBean(R.drawable.simaqian,"太后：令吾百岁之后，皆鱼肉之矣","热点","司马迁"),
            ImgAndTextBean(R.drawable.simaqian,"太后：令吾百岁之后，皆鱼肉之矣","热点","司马迁"),
            ImgAndTextBean(R.drawable.simaqian,"太后：令吾百岁之后，皆鱼肉之矣","热点","司马迁"),
            ImgAndTextBean(R.drawable.simaqian,"太后：令吾百岁之后，皆鱼肉之矣","热点","司马迁"),
            ImgAndTextBean(R.drawable.simaqian,"太后：令吾百岁之后，皆鱼肉之矣","热点","司马迁"),
            ImgAndTextBean(R.drawable.simaqian,"太后：令吾百岁之后，皆鱼肉之矣","热点","司马迁")
        )
        adapter = HomeAdapter(items)
        recyclerView.adapter = adapter
    }
}