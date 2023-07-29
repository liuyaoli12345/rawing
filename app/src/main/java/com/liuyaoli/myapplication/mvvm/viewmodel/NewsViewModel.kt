package com.liuyaoli.myapplication.mvvm.viewmodel

import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.liuyaoli.myapplication.MyApplication
import com.liuyaoli.myapplication.mvvm.model.WeatherData
import com.liuyaoli.myapplication.mvvm.model.entity.NewsBriefEntity
import com.liuyaoli.myapplication.mvvm.model.entity.NewsContentEntity
import com.liuyaoli.myapplication.mvvm.repository.NewsRepository

class NewsViewModel : ViewModel() {
    private val newsRepository = NewsRepository()

    private val _newsBriefsData = MutableLiveData<List<NewsBriefEntity>?>()

    val newsBriefData: LiveData<List<NewsBriefEntity>?>
        get() = _newsBriefsData

    private val _newsContentsData = MutableLiveData<NewsContentEntity?>()

    val newsContentData: LiveData<NewsContentEntity?>
        get() = _newsContentsData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getNewsBriefData(){
        newsRepository.getNewsBriefData(object : NewsRepository.NewsDataCallback {
            override fun onBriefSuccess(newsBriefEntities: List<NewsBriefEntity>?) {
                _newsBriefsData.postValue(newsBriefEntities)
            }

            override fun onContentSuccess(newsContentEntity: NewsContentEntity?) {

            }

//            override fun onFailure(errorMessage: String) {
//                Looper.prepare()
//                Toast.makeText(MyApplication.context,"新闻数据获取失败，可能需要VPN以访问news.org",Toast.LENGTH_LONG).show()
//                Looper.loop()
//            }
        })
    }

    fun getNewsContentData(id : Long){
        newsRepository.getNewsContentData(id, object : NewsRepository.NewsDataCallback{
            override fun onBriefSuccess(newsBriefEntities: List<NewsBriefEntity>?) {

            }

            override fun onContentSuccess(newsContentEntity: NewsContentEntity?) {
                _newsContentsData.postValue(newsContentEntity)
            }

//            override fun onFailure(errorMessage: String) {
//                Looper.prepare()
//                Toast.makeText(MyApplication.context,"新闻数据获取失败，可能需要VPN以访问news.org",Toast.LENGTH_LONG).show()
//                Looper.loop()
//            }
        })
    }
}