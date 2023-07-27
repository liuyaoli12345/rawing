package com.liuyaoli.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.transition.TransitionInflater
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    }

    private fun loadNavBar(){
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val homeFragment = HomeFragment()
        val videoFragment = VideoFragment()
        val mineFragment = MineFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .commit()
        var nextFragment: Fragment? = null
        var curFragment: Fragment = homeFragment
        bottomNav.setOnNavigationItemSelectedListener { item ->
            var tag: String? = null
            when (item.itemId) {
                R.id.action_home -> {
                    nextFragment = homeFragment
                    tag = "home_fragment"
                }
                R.id.action_video -> {
                    nextFragment = videoFragment
                    tag = "video_fragment"
                }
                R.id.action_profile -> {
                    nextFragment = mineFragment
                    tag = "mine_fragment"
                }
            }

            if (nextFragment != null) {
                if (nextFragment!!.isAdded) {
                    supportFragmentManager.beginTransaction()
                        .hide(curFragment)
                        .show(nextFragment!!)
                        .commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .hide(curFragment)
                        .add(R.id.fragment_container, nextFragment!!)
                        .commit()
                }
                curFragment = nextFragment as Fragment
            }
            true
        }
    }

}