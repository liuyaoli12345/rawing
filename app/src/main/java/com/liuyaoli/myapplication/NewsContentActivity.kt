package com.liuyaoli.myapplication

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.liuyaoli.myapplication.mvvm.repository.database.NewsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsContentActivity : AppCompatActivity() {
    private lateinit var db: NewsDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_content_page)
        db = NewsDatabase.getInstance(this)
        val backButton: ImageButton = findViewById(R.id.news_content_back_button)
        showNews()
        backButton.setOnClickListener {
            finish()
        }


    }

    private fun showNews(){
        val newsId = intent.getLongExtra("id",0)
//        Log.i("abcdefg",newsId.toString())
        val titleText = findViewById<TextView>(R.id.news_content_title)
        val contextText = findViewById<TextView>(R.id.news_content_context)
        val authorText = findViewById<TextView>(R.id.news_content_author)
        val abstractText = findViewById<TextView>(R.id.news_content_abstract)
        val headImg = findViewById<ImageView>(R.id.news_content_headImg)
        titleText.text = intent.getStringExtra("title")
        authorText.text = intent.getStringExtra("author")
        GlobalScope.launch(Dispatchers.IO) {
            val newsContent = db.newsContentDao.get(newsId)
            withContext(Dispatchers.Main){
                contextText.text = newsContent?.newsContext
                abstractText.text = newsContent?.newsAbstract
                if (newsContent != null) {
                    Glide.with(MyApplication.context)
                        .load(newsContent.headImgUri)
                        .placeholder(R.drawable.place_holder) // 设置占位图
                        .error(R.drawable.network_err)
                        .timeout(6000)// 设置加载错误时显示的图片
                        .into(headImg)
                }
            }
        }

    }
}