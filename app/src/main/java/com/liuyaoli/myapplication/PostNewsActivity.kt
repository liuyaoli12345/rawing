package com.liuyaoli.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.liuyaoli.myapplication.mvvm.repository.database.NewsDatabase
import com.liuyaoli.myapplication.mvvm.model.entity.NewsBriefEntity
import com.liuyaoli.myapplication.mvvm.model.entity.NewsContentEntity
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
    private var thumbnailUri: String = ""
    private var headImgUri: String = ""
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
//        grantUriPermission("com.liuyaoli.myapplication",Uri.parse("content://com.android.providers.media.documents"),Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
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

                val newsId = db.newsBriefDao.insert(NewsBriefEntity(null,newsTitle,thumbnailUri,"刘尧力","热点"))
                db.newsContentDao.insert(NewsContentEntity(newsId,headImgUri,newsAbstract,newsContext))

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
//            StoragePermissionUtils.requestReadExternalStoragePermission(this)
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            thumbnailsPickImage.launch(intent)
            thumbnailsPickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        uploadHeadImgButton.setOnClickListener {
//            StoragePermissionUtils.requestReadExternalStoragePermission(this)
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            headImgPickImage.launch(intent)
            headImgPickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

    }
    private fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        val cursor = context.contentResolver.query(contentUri, null, null, null, null)
        cursor?.let {
            it.moveToFirst()
            val idx = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val path = it.getString(idx)
            it.close()
            return path
        }
        return null
    }


    private val thumbnailsPickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                    // 在这里展示选择的图片，可以将 URI 传递给相应的 ImageView 或其他展示图片的组件
                    uploadThumbnailsButton.setImageURI(uri)
//                    thumbnailUri = Uri.fromFile(File(getRealPathFromURI(this,uri)!!)).toString()
                    thumbnailUri = uri.toString()
//                    Log.i("ttttt", thumbnailUri)
                }
        }

    private val headImgPickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                // 在这里展示选择的图片，可以将 URI 传递给相应的 ImageView 或其他展示图片的组件
                uploadHeadImgButton.setImageURI(uri)
//                    thumbnailUri = Uri.fromFile(File(getRealPathFromURI(this,uri)!!)).toString()
                headImgUri = uri.toString()
//                Log.i("ttttt", headImgUri)
            }
        }

//    private val headImgPickImage =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val uri: Uri? = result.data?.data
//                // 处理选择的图片
//                if (uri != null) {
//                    // 在这里展示选择的图片，可以将 URI 传递给相应的 ImageView 或其他展示图片的组件
//                    uploadHeadImgButton.setImageURI(uri)
//                    headImgUri = Uri.fromFile(File(getRealPathFromURI(this,uri)!!)).toString()
//                    Log.i("yyyyy",uri.toString())
//                }
//            }
//        }
}