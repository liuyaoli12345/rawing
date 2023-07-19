package com.liuyaoli.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class PostNewsActivity : AppCompatActivity() {

    private lateinit var uploadThumbnailsButton: ImageButton
    private lateinit var uploadHeadImgButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_news_page)
        val backButton: ImageButton = findViewById(R.id.postNewsBackButton)
        backButton.setOnClickListener {
            finish()
        }
        setUploadImageButton()
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

    private val thumbnailsPickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            // 处理选择的图片
            if (uri != null) {
                // 在这里展示选择的图片，可以将 URI 传递给相应的 ImageView 或其他展示图片的组件
               uploadThumbnailsButton.setImageURI(uri)
            }
        }
    }

    private val headImgPickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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