package com.liuyaoli.myapplication

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.liuyaoli.myapplication.mvvm.model.entity.NewsBriefEntity
import com.liuyaoli.myapplication.mvvm.model.entity.NewsContentEntity
import com.liuyaoli.myapplication.mvvm.repository.database.NewsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MyApplication : Application() {

    private lateinit var newsDatabase: NewsDatabase

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        newsDatabase = NewsDatabase.getInstance(this)
        GlobalScope.launch(Dispatchers.IO) {
            val mineId = newsDatabase.newsBriefDao.insert(NewsBriefEntity(null,"About me","https://files.lsmcloud.top/blogtelegram-cloud-photo-size-5-6123103208122464044-y.jpg","刘尧力","欢迎文档"))
            newsDatabase.newsContentDao.insert(NewsContentEntity(mineId,"https://files.lsmcloud.top/blog合照.jpg","about me","- Studying at Nanjing University\n- sophomore\n- Love singing and playing badminton\n- GPA 4.51 9.32%"))
            val documentId = newsDatabase.newsBriefDao.insert(NewsBriefEntity(null,"欢迎使用刘尧力开发的Rawing！点击查看功能介绍:)","https://files.lsmcloud.top/blog9b65014d0cbf8d1f6ee30439d781a927458154784.png@2072w.webp","刘尧力","欢迎文档"))
            newsDatabase.newsContentDao.insert(NewsContentEntity(documentId,"https://files.lsmcloud.top/blog9b65014d0cbf8d1f6ee30439d781a927458154784.png@2072w.webp","Rawing是由刘尧力开发的一个简易浏览器","它包括以下功能:\n1.从news.org获取新闻头条展示在主界面（需要外网访问权限，国内的新闻api要付费)\n2.用户可以发布新闻并显示在首页\n3.通过openWeatherMap api和OkHttp3，并结合用户当前定位，获取用户所在地的天气\n4.引入了Exoplayer、Glide等第三方库处理媒体文件，引入了Room处理本地数据\n5.接入了百度搜索，用户可以在首页输入关键词并搜索\n6.拥有视频流功能，由于没有合适的api所以使用了固定的假数据\n7.在开发过程中，使用profiler对内存泄漏进行了一定的处理，附在README中"))
            val explainId = newsDatabase.newsBriefDao.insert(NewsBriefEntity(null, "每次启动会新增这样三个Bean,目的是在没有VPN的情况下展示recyclerView效果","","刘尧力","欢迎文档"))
            newsDatabase.newsContentDao.insert(NewsContentEntity(explainId,"https://files.lsmcloud.top/blog合照.jpg","说明","每次启动用Rawing都会在OnCreate中新增这样三个Bean，因此多次启动可能发现出现多组这样的Bean，目的是在没有VPN的情况下展示recyclerView效果"))
        }
        context =applicationContext
    }

}