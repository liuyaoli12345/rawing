package com.liuyaoli.myapplication.mainfragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.liuyaoli.myapplication.PostNewsActivity
import com.liuyaoli.myapplication.R
import com.liuyaoli.myapplication.R.*
import com.liuyaoli.myapplication.WeatherActivity
import com.liuyaoli.myapplication.database.NewsDatabase
import com.liuyaoli.myapplication.database.entity.NewsBriefEntity
import com.liuyaoli.myapplication.homerecycler.HomeAdapter
import com.liuyaoli.myapplication.homerecycler.ImgAndTextBean
import com.liuyaoli.myapplication.homerecycler.PlainTextBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private var weatherTransparentAnim: ConstraintLayout? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    private lateinit var view: View
    private lateinit var db: NewsDatabase
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(layout.home_page, container, false)
        db = NewsDatabase.getInstance(this.requireContext())
        showRecyclerView()
        showWeatherTransparent()
        val weatherLayout: ConstraintLayout = view.findViewById(R.id.weather)
        weatherLayout.setOnClickListener {
            // 在此处处理点击事件
            // 在这里执行页面跳转的代码
            val intent = Intent(this.context, WeatherActivity::class.java)
            startActivity(intent)
        }
        val postNewsButton: ImageButton = view.findViewById(R.id.post_news_button)
        postNewsButton.setOnClickListener {
            val intent = Intent(this.context, PostNewsActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        showRecyclerView()
    }

    private fun showWeatherTransparent() {
        weatherTransparentAnim = view.findViewById(R.id.weather)

        val anim = AnimationUtils.loadAnimation(this.context, R.anim.weather_transparent_anim)
        anim.interpolator = LinearInterpolator()
        anim.repeatCount = Animation.INFINITE
        weatherTransparentAnim?.startAnimation(anim)
    }

    private fun showRecyclerView() {
        recyclerView = view.findViewById(R.id.homeRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        var news: List<NewsBriefEntity>?
        val items = mutableListOf<Any>()
        GlobalScope.launch(Dispatchers.IO) {
            news = db.newsBriefDao.getAllNewsBrief()
            withContext(Dispatchers.Main) {
                news?.let {
                    for (item in news!!) {
                        if (item.coverUri.isEmpty()){
                            items.add(PlainTextBean(item.newsId!!,item.title,item.status,item.author))
                        } else {
                            items.add(ImgAndTextBean(item.newsId!!,drawable.simaqian,item.title,item.status,item.author))
                        }
                    }
                }
                adapter = HomeAdapter(items)
                recyclerView.adapter = adapter
            }
        }
    }
}