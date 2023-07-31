package com.liuyaoli.myapplication.mainfragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.liuyaoli.myapplication.LogInActivity
import com.liuyaoli.myapplication.MyApplication
import com.liuyaoli.myapplication.R
import com.liuyaoli.myapplication.mvvm.repository.database.NewsDatabase
import com.liuyaoli.myapplication.mvvm.model.entity.NewsBriefEntity
import com.liuyaoli.myapplication.homeandminerecycler.HomeAndMineAdapter
import com.liuyaoli.myapplication.homeandminerecycler.ImgAndTextBean
import com.liuyaoli.myapplication.homeandminerecycler.PlainTextBean
import com.liuyaoli.myapplication.utils.LoggedInUserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MineFragment : Fragment() {

    //逻辑和主页面相似
    //首先从数据库取得当前用户，再将当前用户的id、用户名渲染到顶部
    //然后将当前用户发布的文章以主界面类型的recyclerView渲染出来

    private lateinit var view: View
    private lateinit var recyclerView: RecyclerView
//    private lateinit var userDb: UserDatabase
    private lateinit var newsDb: NewsDatabase
    private lateinit var adapter: HomeAndMineAdapter
    private lateinit var loginButton: Button
    private var isLoggedIn = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.mine_page, container, false)
        newsDb = NewsDatabase.getInstance(this.requireContext())
        isLoggedIn = LoggedInUserManager.checkIfLoggedIn(this.requireContext())
        loginButton = view.findViewById<Button>(R.id.mine_sign_in_button)
//        userDb = UserDatabase.getInstance(this.requireContext())
        showUserAndRecyclerView()
//        showRecyclerView()
        Log.i("abcdefg", isLoggedIn.toString())
        if (!isLoggedIn) {
            setUpLoginButton()
        } else{
            setLoginToLogOut()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        isLoggedIn = LoggedInUserManager.checkIfLoggedIn(this.requireContext())
        if (!LoggedInUserManager.checkIfLoggedIn(this.requireContext())){
            setUpLoginButton()
            showUserAndRecyclerView()
        } else{
            setLoginToLogOut()
            showUserAndRecyclerView()
        }
//        showRecyclerView()
    }

    private fun setUpLoginButton(){
//        loginButton.visibility = View.VISIBLE
        loginButton.text = "登录"
        loginButton.setOnClickListener {
            val intent = Intent(this.context, LogInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setLoginToLogOut(){
        loginButton.text = "注销"
        loginButton.setOnClickListener {
            LoggedInUserManager.clearUserLoginStatus(this.requireContext())
            onResume()
        }
    }

    private fun showUserAndRecyclerView(){
        val avatar = view.findViewById<ShapeableImageView>(R.id.mine_avatar)
        val userName = view.findViewById<TextView>(R.id.mine_user_name)
        val userId = view.findViewById<TextView>(R.id.mine_user_id)
        if (isLoggedIn){
            val avatarUri = LoggedInUserManager.getLoggedInUserAvatarUri(this.requireContext())
            val author = LoggedInUserManager.getLoggedInUserName(this.requireContext())
            Glide.with(this.requireContext())
                .load(avatarUri)
                .placeholder(R.drawable.place_holder) // 设置占位图
                .error(R.drawable.network_err)
                .timeout(6000)// 设置加载错误时显示的图片
                .into(avatar)
            userName.text = author
            userId.text = LoggedInUserManager.getLoggedInUserId(this.requireContext())
            showRecyclerView(author)
        } else {
            avatar.setImageResource(R.drawable.default_profile)
            userName.text = "登录后显示用户名"
            userId.text = "登录后显示id"
            showRecyclerView("")
        }
    }


    private fun showRecyclerView(author: String) {
        recyclerView = view.findViewById(R.id.mine_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        var news: List<NewsBriefEntity>?
        val items = mutableListOf<Any>()
        if (author.isEmpty()){
            items.clear()
            adapter = HomeAndMineAdapter(items)
            recyclerView.adapter = adapter
            return
        }
        GlobalScope.launch(Dispatchers.IO) {
            news = newsDb.newsBriefDao.findNewsBriefByUserName(author)
            items.clear()
            withContext(Dispatchers.Main) {
                news?.let {
                    for (item in news!!) {
                        if (item.coverUri.isEmpty()){
                            items.add(PlainTextBean(item.newsId!!,item.title,item.status,item.author))
                        } else {
                            items.add(
                                ImgAndTextBean(item.newsId!!,
                                    item.coverUri,item.title,item.status,item.author)
                            )
                        }
                    }
                }
//                Log.i("ttttt","准备更新recyclerView")
                adapter = HomeAndMineAdapter(items)
                recyclerView.adapter = adapter
            }
        }
    }
}