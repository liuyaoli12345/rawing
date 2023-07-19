package com.liuyaoli.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.liuyaoli.myapplication.homerecycler.HomeAdapter
import com.liuyaoli.myapplication.homerecycler.ImgAndTextBean
import com.liuyaoli.myapplication.homerecycler.PlainTextBean
import com.liuyaoli.myapplication.mainfragments.HomeFragment
import com.liuyaoli.myapplication.mainfragments.ProfileFragment
import com.liuyaoli.myapplication.mainfragments.VideoFragment

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
//        showWeatherTransparent()
//        showRecyclerView()
        loadNavBar()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()
    }

    private fun loadNavBar(){
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            var fragment: Fragment? = null
            when (item.itemId) {
                R.id.action_home -> {
                    fragment = HomeFragment()
                }
                R.id.action_video -> {
                    fragment = VideoFragment()
                }
                R.id.action_profile -> {
                    fragment = ProfileFragment()
                }
            }

            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            }

            true
        }
    }

}