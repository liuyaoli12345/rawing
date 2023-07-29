package com.liuyaoli.myapplication.mainfragments

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.liuyaoli.myapplication.PostNewsActivity
import com.liuyaoli.myapplication.R
import com.liuyaoli.myapplication.R.*
import com.liuyaoli.myapplication.WeatherActivity
import com.liuyaoli.myapplication.constants.PermissionConstants
import com.liuyaoli.myapplication.mvvm.repository.database.NewsDatabase
import com.liuyaoli.myapplication.homeandminerecycler.HomeAndMineAdapter
import com.liuyaoli.myapplication.homeandminerecycler.ImgAndTextBean
import com.liuyaoli.myapplication.homeandminerecycler.PlainTextBean
import com.liuyaoli.myapplication.mvvm.viewmodel.NewsViewModel
import com.liuyaoli.myapplication.mvvm.viewmodel.WeatherViewModel



class HomeFragment : Fragment() {
    private var weatherTransparentAnim: ConstraintLayout? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAndMineAdapter
    private lateinit var view: View
    private lateinit var db: NewsDatabase
    private lateinit var searchBar: EditText
    private lateinit var homeMotion: MotionLayout
    private lateinit var searchFoundList: ListView
    private lateinit var webView: WebView
    private lateinit var weatherLayout: ConstraintLayout
    private val weatherViewModel by viewModels<WeatherViewModel>()
    private val newsViewModel by viewModels<NewsViewModel>()
    private var searchContent = ""
    private var TAG = "abcdefg"
    private var mStrings = listOf(
        "蔡徐坤真的塌房了吗？", "许家印将一切上交国家，每一个家庭要为此承担多少？", "台风登录厦门", "Meta放出大招，开源大模型有何影响？", "bilibili决定不再以视频播放量评判视频热度"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(layout.home_page, container, false)
        db = NewsDatabase.getInstance(this.requireContext())
        searchBar = view.findViewById(R.id.home_page_search)
        homeMotion = view.findViewById(R.id.home_page_motion_layout)
        searchFoundList = view.findViewById(R.id.search_list_view)
        weatherLayout = view.findViewById(R.id.home_page_weather)
        setUpSearchBar()
        showRecyclerView()
        showWeatherTransparent()
        setUpWebView()

        // Get the user's current location's latitude and longitude (You should implement this part)

        // Observe the weather data and update UI when data changes
        weatherViewModel.weatherData.observe(this.viewLifecycleOwner, Observer { weatherData ->
            val temperatureTextView =
                weatherLayout.findViewById<TextView>(R.id.home_page_temperature)
            temperatureTextView.text = "${weatherData.temperature}˚"
            val weatherDescriptionTextView =
                weatherLayout.findViewById<TextView>(R.id.home_page_site_weather)
            weatherDescriptionTextView.text = weatherData.weatherDescription
            val weatherFeelsLikeTempTextView =
                weatherLayout.findViewById<TextView>(R.id.home_page_feels_like_temp)
            weatherFeelsLikeTempTextView.text = "体感${weatherData.feelsLikeTemp}˚"
        })

        // Observe the error message and handle errors if any
        weatherViewModel.errorMessage.observe(this.viewLifecycleOwner, Observer { errorMessage ->
            // Handle the error, e.g., show a toast or a dialog
            // You may also want to implement some retry mechanism
            Toast.makeText(this.requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })

        getWeatherData()


        val weatherLayout: ConstraintLayout = view.findViewById(R.id.home_page_weather)
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

    override fun onDestroyView() {
        recyclerView.adapter = null
        super.onDestroyView()
    }

    private var queryLocationAgain = false
    private fun getWeatherData() {
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())
        Log.i("qwerty", "enter getWeatherData()")
        if (ContextCompat.checkSelfPermission(
                this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted, get the user's location
            val locationRequest = LocationRequest().setInterval(200000).setFastestInterval(200000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            fusedLocationClient.requestLocationUpdates(
                locationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {
                            weatherViewModel.getWeatherData(location.latitude, location.longitude)
                            Log.i("qwerty", "Try get weather data")
                        }
                        fusedLocationClient.removeLocationUpdates(this)
                    }
                }, Looper.myLooper()
            )
        } else if (!queryLocationAgain) {
            // Request location permission
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PermissionConstants.LOCATION_PERMISSION_CODE
            )
            getWeatherData()
        }
    }


    private fun setUpWebView() {
        webView = view.findViewById<WebView>(R.id.search_res_page)

        // 声明 WebSettings子类
        val webSettings = webView.getSettings()
        // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true)
        // 缩放操作
        webSettings.setSupportZoom(true)   // 支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true)  // 设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false)   // 隐藏原生的缩放控件
        // 其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)  // 关闭webview中缓存
        webSettings.setAllowFileAccess(true)    // 设置可以访问文件
        webSettings.setDefaultTextEncodingName("utf-8")   // 设置编码格式

        // webView?.loadUrl("https://ww.baidu.com]")  // 加载百度首页
        webView.loadUrl("https://m.baidu.com")  // 加载百度结果页

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // 设置不用系统浏览器打开,直接显示在当前Webview
                Log.i(TAG, "shouldOverrideUrlLoading :" + url)

                if (url.startsWith("http")) {
                    view.loadUrl(url)
                }
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // 设定加载开始的操作
                // 我们可以设定一个loading的页面，告诉用户网页在加载中。
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                // 设定可视阶段的操作
                // webview可视阶段调用，指后端返回数据，webview开始渲染时回调，当前一般还是白屏状态。
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                // 设定加载结束的操作
                // 这个时候的页面已经加载完成，核心的JS也已经运行，可以关闭loading条
            }

            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                super.doUpdateVisitedHistory(view, url, isReload)
                // webview历史变更的回调，告诉webview历史记录更新。
                Log.i(TAG, "doUpdateVisitedHistory :" + url)
            }

            override fun onReceivedError(
                view: WebView, request: WebResourceRequest, error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedHttpError(
                view: WebView, request: WebResourceRequest, errorResponse: WebResourceResponse
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onReceivedSslError(
                view: WebView, handler: SslErrorHandler, error: SslError
            ) {
                super.onReceivedSslError(view, handler, error)
            }
        }

        webView!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, progress: Int) {
                super.onProgressChanged(view, progress)
                Log.i(TAG, "onProgressChanged :" + progress)
            }

            override fun onReceivedTitle(view: WebView?, title: String) {
                super.onReceivedTitle(view, title)
                Log.i(TAG, "onReceivedTitle :" + title)
            }
        }

    }


    private fun setUpSearchBar() {

        var isOnSearch = false
        // 监听搜索按钮点击事件
        searchBar.setOnClickListener {
            // 当搜索按钮被点击时，触发上升动画
            homeMotion.transitionToState(R.id.end)
            isOnSearch = true
            Log.i("gggg", "搜索框被点击")
        }

        searchBar.doAfterTextChanged {
            it?.let {
                if (it.isEmpty()) {
                    searchFoundList.clearTextFilter()
                } else {
                    searchFoundList.setFilterText(it.toString())
                }
            }
            searchContent = it.toString()
        }

        val searchButton = view.findViewById<Button>(R.id.home_page_search_button)

        searchButton.setOnClickListener {
            isOnSearch = true
            val uri = "https://m.baidu.com/s?word=$searchContent"
            homeMotion.transitionToState(R.id.searching)
            webView.loadUrl(uri)
            Log.i("gggg", "搜索按钮被点击")
        }

        searchFoundList.setAdapter(
            ArrayAdapter<Any>(
                this.requireContext(), android.R.layout.simple_list_item_1, mStrings
            )
        )
        searchFoundList.setTextFilterEnabled(true)
        searchFoundList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectText = mStrings[position]
            searchBar.setText(selectText)
        }


        val callback = requireActivity().addOnBackPressed {
            if (isOnSearch) {
                isOnSearch = false
                homeMotion.transitionToState(R.id.start)
                Log.i("gggg", "搜索框返回")
                true
            } else false
        }


    }

    private fun showWeatherTransparent() {
        weatherTransparentAnim = view.findViewById(R.id.home_page_weather)

        val anim = AnimationUtils.loadAnimation(this.context, R.anim.weather_transparent_anim)
        anim.interpolator = LinearInterpolator()
        anim.repeatCount = Animation.INFINITE
        weatherTransparentAnim?.startAnimation(anim)
    }

    private fun showRecyclerView() {
        recyclerView = view.findViewById(R.id.homeRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        var items = mutableListOf<Any>()
        newsViewModel.newsBriefData.observe(this.requireActivity(), Observer { newsBriefData ->
            items.clear()
            if (newsBriefData != null) {
                for (item in newsBriefData){
                    if (item.coverUri.isEmpty()) {
                        items.add(PlainTextBean(item.newsId!!, item.title, item.status, item.author))
                    } else {
                        items.add(ImgAndTextBean(item.newsId!!, item.coverUri, item.title, item.status, item.author))
                    }
                }
            }
            adapter = HomeAndMineAdapter(items)
            recyclerView.adapter = adapter
        })
        newsViewModel.getNewsBriefData()
    }

    /**
     * 绑定返回键回调（建议使用该方法）
     * @param owner Receive callbacks to a new OnBackPressedCallback when the given LifecycleOwner is at least started.
     * This will automatically call addCallback(OnBackPressedCallback) and remove the callback as the lifecycle state changes. As a corollary, if your lifecycle is already at least started, calling this method will result in an immediate call to addCallback(OnBackPressedCallback).
     * When the LifecycleOwner is destroyed, it will automatically be removed from the list of callbacks. The only time you would need to manually call OnBackPressedCallback.remove() is if you'd like to remove the callback prior to destruction of the associated lifecycle.
     * @param onBackPressed 回调方法；返回true则表示消耗了按键事件，事件不会继续往下传递，相反返回false则表示没有消耗，事件继续往下传递
     * @return 注册的回调对象，如果想要移除注册的回调，直接通过调用[OnBackPressedCallback.remove]方法即可。
     */
    fun androidx.activity.ComponentActivity.addOnBackPressed(
        owner: LifecycleOwner, onBackPressed: () -> Boolean
    ): OnBackPressedCallback {
        return backPressedCallback(onBackPressed).also {
            onBackPressedDispatcher.addCallback(owner, it)
        }
    }

    /**
     * 绑定返回键回调，未关联生命周期，建议使用关联生命周期的办法（尤其在fragment中使用，应该关联fragment的生命周期）
     */
    fun androidx.activity.ComponentActivity.addOnBackPressed(onBackPressed: () -> Boolean): OnBackPressedCallback {
        return backPressedCallback(onBackPressed).also {
            onBackPressedDispatcher.addCallback(it)
        }
    }

    private fun androidx.activity.ComponentActivity.backPressedCallback(onBackPressed: () -> Boolean): OnBackPressedCallback {
        return object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!onBackPressed()) {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        }
    }

}