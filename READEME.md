# READEME

```
它包括以下功能:
1.从news.org获取新闻头条展示在主界面（需要外网访问权限，国内的新闻api要付费)
2.用户可以发布新闻并显示在首页
3.通过openWeatherMap api和OkHttp3，并结合用户当前定位，获取用户所在地的天气
4.引入了Exoplayer、Glide等第三方库处理媒体文件，引入了Room处理本地数据
5.接入了百度搜索，用户可以在首页输入关键词并搜索
6.拥有视频流功能，由于没有合适的api所以使用了固定的假数据
7.在开发过程中，使用profiler对内存泄漏进行了一定的处理，附在README中"
8.使用了file_provider为用户提供图片选择
```

Profile 分析发现weather Activity中

```kotlin
 if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted, get the user's location
            val locationRequest = LocationRequest().setInterval(200000).setFastestInterval(200000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {
                            weatherViewModel.getWeatherData(location.latitude, location.longitude)
//                            Log.i("qwerty", "Try get weather data")
                        }
                        fusedLocationClient.removeLocationUpdates(this)
                    }
                },
                Looper.myLooper()
            )
```

中出现了强引用

通过fusedLocationClient.removeLocationUpdates(this)使得资源即使释放，解决了问题