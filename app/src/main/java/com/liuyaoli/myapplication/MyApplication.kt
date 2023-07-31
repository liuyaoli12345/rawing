package com.liuyaoli.myapplication

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.media3.common.util.Log
import com.liuyaoli.myapplication.constants.UserConstants
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
        context =applicationContext
        newsDatabase = NewsDatabase.getInstance(this)
            GlobalScope.launch(Dispatchers.IO) {
                val mineId = newsDatabase.newsBriefDao.insert(
                    NewsBriefEntity(
                        null,
                        "About me",
                        "https://files.lsmcloud.top/blogtelegram-cloud-photo-size-5-6123103208122464044-y.jpg",
                        "刘尧力",
                        "欢迎文档"
                    )
                )
                newsDatabase.newsContentDao.insert(
                    NewsContentEntity(
                        mineId,
                        "https://files.lsmcloud.top/blog合照.jpg",
                        "about me",
                        "- Studying at Nanjing University\n- sophomore\n- Love singing and playing badminton\n- GPA 4.51 9.32%"
                    )
                )
                val environmentId = newsDatabase.newsBriefDao.insert(
                    NewsBriefEntity(
                        null,
                        "开发时使用的机型是Redmi K40，Android 版本 11+，MIUI 12.0.7",
                        "https://files.lsmcloud.top/blog202307312045536.png",
                        "刘尧力",
                        "欢迎文档"
                    )
                )
                newsDatabase.newsContentDao.insert(
                    NewsContentEntity(
                        environmentId,
                        "https://files.lsmcloud.top/blog202307312044996.png",
                        "开发环境说明",
                        "开发时由于电脑存储空间不足，所以没有下载模拟器调试，直接使用的真机调试~"
                    )
                )
                val needVPNId = newsDatabase.newsBriefDao.insert(
                    NewsBriefEntity(
                        null,
                        "首页的新闻接入了news.org api(国内api要付费)，可能需要VPN才能获取到数据:(",
                        "https://files.lsmcloud.top/blog202307312032971.png",
                        "刘尧力",
                        "欢迎文档"
                    )
                )
                newsDatabase.newsContentDao.insert(
                    NewsContentEntity(
                        needVPNId,
                        "https://files.lsmcloud.top/blog202307312035219.png",
                        "新闻api说明",
                        "新闻api仅作为展示用途，获取到的任何内容都不代表软件作者的立场"
                    )
                )
                val documentId = newsDatabase.newsBriefDao.insert(
                    NewsBriefEntity(
                        null,
                        "欢迎使用刘尧力开发的Rawing！点击查看功能介绍:)",
                        "https://files.lsmcloud.top/blog9b65014d0cbf8d1f6ee30439d781a927458154784.png@2072w.webp",
                        "刘尧力",
                        "欢迎文档"
                    )
                )
                newsDatabase.newsContentDao.insert(
                    NewsContentEntity(
                        documentId,
                        "https://files.lsmcloud.top/blog9b65014d0cbf8d1f6ee30439d781a927458154784.png@2072w.webp",
                        "Rawing是由刘尧力开发的一个简易浏览器",
                        "它包括以下功能:\n1.从news.org获取新闻头条展示在主界面（需要外网访问权限，国内的新闻api要付费)\n2.用户可以发布新闻并显示在首页\n3.通过openWeatherMap api和OkHttp3，并结合用户当前定位，获取用户所在地的天气\n4.引入了Exoplayer、Glide等第三方库处理媒体文件，引入了Room处理本地数据\n5.接入了百度搜索，用户可以在首页输入关键词并搜索\n6.拥有视频流功能，由于没有合适的api所以使用了固定的假数据\n7.在开发过程中，使用profiler对内存泄漏进行了一定的处理，附在README中"
                    )
                )
                val explainId = newsDatabase.newsBriefDao.insert(
                    NewsBriefEntity(
                        null,
                        "每次打开app都会添置5个bean用来在没有VPN时展示recyclerView~",
                        "",
                        "刘尧力",
                        "欢迎文档"
                    )
                )
                newsDatabase.newsContentDao.insert(
                    NewsContentEntity(
                        explainId,
                        "https://files.lsmcloud.top/blog合照.jpg",
                        "说明",
                        "这是百度课程大作业成品，仅做学习用途，使用的图标资源与api均符合使用标准，如有侵权请联系linshuoming12345@163.com删除"
                    )
                )
            }
        }
    }
