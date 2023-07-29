package com.liuyaoli.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.imageview.ShapeableImageView
import com.liuyaoli.myapplication.mvvm.repository.database.UserDatabase
import com.liuyaoli.myapplication.mvvm.model.entity.UserEntity
import com.liuyaoli.myapplication.utils.Encrypter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SingUpActivity : AppCompatActivity() {

    private lateinit var userDb: UserDatabase
    private lateinit var userName: String
    private lateinit var userPwd: String
    private lateinit var userId: String
    private lateinit var userAvatar: String
    private lateinit var avatarButton: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_page)
        userDb = UserDatabase.getInstance(this)
        onWatchUserName()
        onWatchUserId()
        onWatchUserPwd()
        setUpSignUpButton()
        setUpAvatar()
    }

    private fun setUpAvatar(){
        avatarButton = findViewById<ShapeableImageView>(R.id.sign_up_avatar)
        avatarButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            avatarPickImage.launch(intent)
        }
    }

    private fun onWatchUserName(){
        val userNameEditText = findViewById<EditText>(R.id.sign_up_user_name)
        userNameEditText.doAfterTextChanged {
            userName = it.toString()
        }
    }

    private fun onWatchUserPwd(){
        val userPwdEditText = findViewById<EditText>(R.id.sign_up_pwd)
        userPwdEditText.doAfterTextChanged {
            userPwd = Encrypter.encrypt(it.toString())
        }
    }

    private fun onWatchUserId(){
        val userIdEditText = findViewById<EditText>(R.id.sign_up_user_id)
        userIdEditText.doAfterTextChanged {
            userId = it.toString()
        }
    }

    private fun setUpSignUpButton(){
        val signUpButton = findViewById<Button>(R.id.sign_up_register_button)
        signUpButton.setOnClickListener {
            if (userName.isEmpty() || userPwd.isEmpty() || userId.isEmpty() || userAvatar.isEmpty()){
                Toast.makeText(this, "用户名、密码、用户id、用户头像不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            GlobalScope.launch {
                userDb.userDao.insert(UserEntity(userId,userName,userAvatar))
                withContext(Dispatchers.Main){
                    //显示"注册成功！"
                    Toast.makeText(this@SingUpActivity, "注册成功！", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private val avatarPickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                // 处理选择的图片
                if (uri != null) {
                    // 在这里展示选择的图片，可以将 URI 传递给相应的 ImageView 或其他展示图片的组件
                    avatarButton.setImageURI(uri)
                    userAvatar = uri.toString()
                }
            }
        }
}