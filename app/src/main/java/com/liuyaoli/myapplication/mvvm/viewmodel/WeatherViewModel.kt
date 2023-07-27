package com.liuyaoli.myapplication.mvvm.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.liuyaoli.myapplication.mvvm.model.WeatherData
import com.liuyaoli.myapplication.mvvm.repository.WeatherRepository

class WeatherViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository()

    private val _weatherData = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData>
        get() = _weatherData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getWeatherData(latitude: Double, longitude: Double) {
        weatherRepository.getWeatherData(latitude, longitude, object : WeatherRepository.WeatherDataCallback {
            override fun onSuccess(weatherData: WeatherData) {
                _weatherData.postValue(weatherData)
            }

            override fun onFailure(errorMessage: String) {
                _errorMessage.postValue(errorMessage)
            }
        })
    }
}
