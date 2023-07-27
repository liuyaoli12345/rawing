package com.liuyaoli.myapplication

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.transition.TransitionInflater
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.liuyaoli.myapplication.constants.PermissionConstants
import com.liuyaoli.myapplication.mainfragments.HomeFragment
import com.liuyaoli.myapplication.mainfragments.MineFragment
import com.liuyaoli.myapplication.mainfragments.VideoFragment

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
//        showWeatherTransparent()
//        showRecyclerView()
        val transition = TransitionInflater.from(this)
            .inflateTransition(android.R.transition.move)
        window.sharedElementEnterTransition = transition

        loadNavBar()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()
    }

    private fun loadNavBar(){
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            var fragment: Fragment? = null
            var tag: String? = null
            when (item.itemId) {
                R.id.action_home -> {
                    fragment = HomeFragment()
                    tag = "home_fragment"
                }
                R.id.action_video -> {
                    fragment = VideoFragment()
                    tag = "video_fragment"
                }
                R.id.action_profile -> {
                    fragment = MineFragment()
                    tag = "mine_fragment"
                }
            }

            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag)
                    .addToBackStack(null)
                    .commit()
            }

            true
        }
    }

}