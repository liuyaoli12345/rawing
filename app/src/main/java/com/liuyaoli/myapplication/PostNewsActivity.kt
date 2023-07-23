package com.liuyaoli.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.liuyaoli.myapplication.database.NewsDatabase
import com.liuyaoli.myapplication.database.entity.NewsBriefEntity
import com.liuyaoli.myapplication.database.entity.NewsContentEntity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostNewsActivity : AppCompatActivity() {

    private lateinit var uploadThumbnailsButton: ImageButton
    private lateinit var uploadHeadImgButton: ImageButton
    private var newsTitle: String = ""
    private var newsContext: String = ""
    private var newsAbstract: String = ""
    private lateinit var db: NewsDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_news_page)
        val backButton: ImageButton = findViewById(R.id.postNewsBackButton)
        backButton.setOnClickListener {
            finish()
        }
        db = NewsDatabase.getInstance(this)
        setUploadImageButton()
        onWatchTitle()
        onWatchAbstract()
        onWatchContext()
        setUpOkButton()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setUpOkButton(){
        val okButton: ImageButton = findViewById(R.id.post_news_ok_button)
        okButton.setOnClickListener {
            if (newsTitle.isEmpty() || newsAbstract.isEmpty() || newsContext.isEmpty()){
                Toast.makeText(this, "标题、摘要、正文不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 在子线程中执行数据库操作
            GlobalScope.launch(Dispatchers.IO) {

                val newsId = db.newsBriefDao.insert(NewsBriefEntity(null,newsTitle,"","刘尧力","热点"))
                db.newsContentDao.insert(NewsContentEntity(newsId,"",newsAbstract,newsContext))

                // UI操作需要回到主线程
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PostNewsActivity, "发布成功", Toast.LENGTH_SHORT).show()
                    finish()
                }

            }
        }
    }

    private fun onWatchAbstract() {
        val newsAbstractElement: EditText = findViewById(R.id.post_news_abstract)
        newsAbstractElement.doAfterTextChanged {
            newsAbstract = it.toString()
        }
    }

    private fun onWatchContext() {
        val newsContextElement: EditText = findViewById(R.id.post_news_context)
        newsContextElement.doAfterTextChanged {
//            Log.i("abcdefg", "changee text!!!")
            newsContext = it.toString()
        }
    }

    private fun onWatchTitle() {
        val newsTitleElement: EditText = findViewById(R.id.post_news_title)
        newsTitleElement.doAfterTextChanged {
            newsTitle = it.toString()
        }
//        newsTitleElement.setText("FaQ!")
//        Log.i("abcdefg","尝试触发监听回调")
    }

    private fun setUploadImageButton() {
        uploadThumbnailsButton = findViewById(R.id.upload_thumbnails_button)
        uploadHeadImgButton = findViewById(R.id.upload_head_img_button)
        uploadThumbnailsButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            thumbnailsPickImage.launch(intent)
        }
        uploadHeadImgButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            headImgPickImage.launch(intent)
        }

    }

    private val thumbnailsPickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                // 处理选择的图片
                if (uri != null) {
                    // 在这里展示选择的图片，可以将 URI 传递给相应的 ImageView 或其他展示图片的组件
                    uploadThumbnailsButton.setImageURI(uri)
                }
            }
        }

    private val headImgPickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                // 处理选择的图片
                if (uri != null) {
                    // 在这里展示选择的图片，可以将 URI 传递给相应的 ImageView 或其他展示图片的组件
                    uploadHeadImgButton.setImageURI(uri)
                }
            }
        }
}