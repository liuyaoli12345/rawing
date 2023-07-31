package com.liuyaoli.myapplication.mvvm.repository

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import com.liuyaoli.myapplication.mvvm.model.WeatherData
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class WeatherRepository {

    private val apiKey = "fb050c88020be2efcae3e6cedfc06d48"

    interface WeatherDataCallback {
        fun onSuccess(weatherData: WeatherData)
        fun onFailure(errorMessage: String)
    }

    fun getWeatherData(latitude: Double, longitude: Double, callback: WeatherDataCallback) {
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$apiKey&units=metric&lang=zh_cn"

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseData = response.body?.string()
                if (response.isSuccessful && !responseData.isNullOrEmpty()) {
                    val weatherData = parseWeatherData(responseData)
                    callback.onSuccess(weatherData)
//                    Log.i("qwerty", responseData.toString())
                } else {
                    callback.onFailure("Failed to fetch weather data")
//                    Log.i("qwerty", "request failed")
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback.onFailure("Network error: ${e.message}")
            }
        })
    }

    private fun parseWeatherData(responseData: String): WeatherData {
        val jsonObject = JSONObject(responseData)
        val temperature = jsonObject.getJSONObject("main").getDouble("temp").toInt()
        val weatherDescription = jsonObject.getJSONArray("weather")
            .getJSONObject(0)
            .getString("description")
        val maxTemp = jsonObject.getJSONObject("main").getDouble("temp_max").toInt()
        val minTemp = jsonObject.getJSONObject("main").getDouble("temp_min").toInt()
        val feelsLikeTemp = jsonObject.getJSONObject("main").getDouble("feels_like").toInt()
        val latitude = jsonObject.getJSONObject("coord").getDouble("lat")
        val longitude = jsonObject.getJSONObject("coord").getDouble("lon")
        return WeatherData(temperature, weatherDescription, maxTemp, minTemp, feelsLikeTemp, latitude, longitude)
    }

}
