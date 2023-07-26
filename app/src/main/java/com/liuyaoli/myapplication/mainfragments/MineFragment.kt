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
import com.liuyaoli.myapplication.WeatherActivity
import com.liuyaoli.myapplication.database.NewsDatabase
import com.liuyaoli.myapplication.database.UserDatabase
import com.liuyaoli.myapplication.database.entity.NewsBriefEntity
import com.liuyaoli.myapplication.homeandminerecycler.HomeAndMineAdapter
import com.liuyaoli.myapplication.homeandminerecycler.ImgAndTextBean
import com.liuyaoli.myapplication.homeandminerecycler.PlainTextBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    //逻辑和主页面相似
    //首先从数据库取得当前用户，再将当前用户的id、用户名渲染到顶部
    //然后将当前用户发布的文章以主界面类型的recyclerView渲染出来

    private lateinit var view: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var userDb: UserDatabase
    private lateinit var newsDb: NewsDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.home_page, container, false)
        newsDb = NewsDatabase.getInstance(this.requireContext())
        showRecyclerView()
        return view
    }

    override fun onResume() {
        super.onResume()
        showRecyclerView()
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
                            items.add(
                                ImgAndTextBean(item.newsId!!,
                                    R.drawable.simaqian,item.title,item.status,item.author)
                            )
                        }
                    }
                }
                adapter = HomeAndMineAdapter(items)
                recyclerView.adapter = adapter
            }
        }
    }
}