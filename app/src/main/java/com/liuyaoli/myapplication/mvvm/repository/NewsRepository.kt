package com.liuyaoli.myapplication.mvvm.repository

import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.liuyaoli.myapplication.MyApplication
import com.liuyaoli.myapplication.NewsContentActivity
import com.liuyaoli.myapplication.R
import com.liuyaoli.myapplication.homeandminerecycler.ImgAndTextBean
import com.liuyaoli.myapplication.homeandminerecycler.PlainTextBean
import com.liuyaoli.myapplication.mvvm.model.WeatherData
import com.liuyaoli.myapplication.mvvm.model.entity.NewsBriefEntity
import com.liuyaoli.myapplication.mvvm.model.entity.NewsContentEntity
import com.liuyaoli.myapplication.mvvm.repository.database.NewsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class NewsRepository {
    private val apiKey = "2144eef56d004e378fbb91126f2b6269"
    private val newsDatabase = NewsDatabase.getInstance(MyApplication.context)

    interface NewsDataCallback {
        fun onBriefSuccess(newsBriefEntities: List<NewsBriefEntity>?)
        fun onContentSuccess(newsContentEntity: NewsContentEntity?)
//        fun onFailure(errorMessage: String)
    }

    fun getNewsBriefData(callback: NewsDataCallback) {
        var news: List<NewsBriefEntity>?
        GlobalScope.launch(Dispatchers.IO) {
            getNewsFromWebToDatabase(callback)
            news = newsDatabase.newsBriefDao.getAllNewsBrief()
//            withContext(Dispatchers.Main) {
               callback.onBriefSuccess(news)
//            }
        }
    }

    fun getNewsContentData(id:Long,callback: NewsDataCallback) {
        var news: NewsContentEntity?
        GlobalScope.launch(Dispatchers.IO) {
            news = newsDatabase.newsContentDao.get(id)
//            withContext(Dispatchers.Main) {
                callback.onContentSuccess(news)
//            }
        }
    }


    private fun getNewsFromWebToDatabase(callback: NewsDataCallback) {
        val url = "https://newsapi.org/v2/top-headlines?country=us&language=en&apiKey=$apiKey"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).enqueue(object : okhttp3.Callback{
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response){
                val json = response.body?.string()?.let { JSONObject(it) }
                json?.let {
                    Log.i("qwerty", "获取news成功")
                    val sourceArray = json.getJSONArray("articles")
                    for (i in 0 until sourceArray.length()) {
                        val curNews = sourceArray.get(i) as JSONObject
                        val author = curNews.get("author").toString()
                        val title = curNews.get("title").toString()
                        val coverUri = curNews.get("urlToImage").toString()
                        val abstract = curNews.get("description").toString()
                        val context = curNews.get("content").toString()
                        Log.i("qwerty",title)
                        val newsId = newsDatabase.newsBriefDao.insert(
                            NewsBriefEntity(
                                null, title, coverUri, author, "头条"
                            )
                        )
                        newsDatabase.newsContentDao.insert(
                            NewsContentEntity(
                                newsId, coverUri, abstract, context
                            )
                        )
                    }
                }
            }
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Looper.prepare()
                Toast.makeText(MyApplication.context,"新闻数据获取失败，可能需要VPN以访问news.org", Toast.LENGTH_LONG).show()
                Looper.loop()
            }
        })

    }

}