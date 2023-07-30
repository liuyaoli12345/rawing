package com.liuyaoli.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.liuyaoli.myapplication.mvvm.repository.database.UserDatabase
import com.liuyaoli.myapplication.utils.Encrypter
import com.liuyaoli.myapplication.utils.LoggedInUserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogInActivity: AppCompatActivity() {

    private lateinit var userDb: UserDatabase
    private var userName = ""
    private var userPwd = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        val backButton = findViewById<ImageButton>(R.id.login_back_button)
        backButton.setOnClickListener {
            finish()
        }
        userDb = UserDatabase.getInstance(this)
        setUpLoginAndSignUpButton()
        onWatchUserName()
        onWatchPassword()
    }

    private fun onWatchUserName(){
        val userNameEditText = findViewById<EditText>(R.id.etUsername)
        userNameEditText.doAfterTextChanged {
            userName = it.toString()
        }
    }

    private fun onWatchPassword(){
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        passwordEditText.doAfterTextChanged {
            userPwd = Encrypter.encrypt(it.toString())
        }
    }

    private fun setUpLoginAndSignUpButton(){
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val signUpButton = findViewById<Button>(R.id.btnRegister)
        loginButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val user = userDb.userDao.findByUserName(userName)
                if (user == null){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@LogInActivity,"用户不存在！",Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
//                if (user.pwd != userPwd){
//                    withContext(Dispatchers.Main){
//                        Toast.makeText(this@LoginActivity,"密码错误！",Toast.LENGTH_SHORT).show()
//                    }
//                    return@launch
//                }
                LoggedInUserManager.saveUserLoginStatus(this@LogInActivity,userName,user.userId,user.avatarUri,true)
                withContext(Dispatchers.Main){
                    Toast.makeText(this@LogInActivity,"登录成功！",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
        signUpButton.setOnClickListener {
            intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}