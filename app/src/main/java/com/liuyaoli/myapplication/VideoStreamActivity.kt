package com.liuyaoli.myapplication

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.ui.PlayerView
import androidx.media3.exoplayer.ExoPlayer


class VideoStreamActivity : AppCompatActivity() {
    private lateinit var player: ExoPlayer
    private var videoId = 0

    private val videoUri = listOf<String>(
        "https://files.lsmcloud.top/xyq_playing.mp4",
        "https://files.lsmcloud.top/dog_and_cat_playing.mp4",
        "https://files.lsmcloud.top/guojichen.mp4",
        "https://files.lsmcloud.top/ewujushi.mp4",
        "https://files.lsmcloud.top/huaqingmaigua.mp4",
        "https://files.lsmcloud.top/aimakevideo.mp4"
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_stream_page)
        val backButton: ImageButton = findViewById(R.id.video_stream_back_button)
        backButton.setOnClickListener {
            finish()
        }
//        initDetector()
        videoId = intent.getIntExtra("videoId", 0)
        playVideoStream()
        setUpButtons()
    }

    private fun setUpButtons() {
        val likeButton = findViewById<ImageButton>(R.id.video_stream_like_button)
        likeButton.setOnClickListener {
            val originalDrawable = likeButton.drawable
            val newColor = resources.getColor(R.color.red, null)
            val colorFilter = PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN)
            originalDrawable.colorFilter = colorFilter
            likeButton.setImageDrawable(originalDrawable)
        }
        val commentButton = findViewById<ImageButton>(R.id.video_stream_comment_button)
        commentButton.setOnClickListener {
            val originalDrawable = commentButton.drawable
            val newColor = resources.getColor(R.color.orange, null)
            val colorFilter = PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN)
            originalDrawable.colorFilter = colorFilter
            commentButton.setImageDrawable(originalDrawable)
        }
        val followButton = findViewById<ImageButton>(R.id.video_stream_follow_button)
        followButton.setOnClickListener {
            followButton.setImageResource(R.drawable.followed_button)
        }
        val nextButton = findViewById<ImageButton>(R.id.video_next_button)
        val lastButton = findViewById<ImageButton>(R.id.video_last_button)
        nextButton.setOnClickListener {
            if (videoId < videoUri.size - 1) {
                playVideo(videoUri[++videoId])
            } else {
                Toast.makeText(this, "已经是最后一个视频了", Toast.LENGTH_SHORT).show()
            }
        }
        lastButton.setOnClickListener {
            if (videoId > 0) {
                playVideo(videoUri[--videoId])
            } else {
                Toast.makeText(this, "已经是第一个视频了", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun playVideoStream() {
        val playerView = findViewById<PlayerView>(R.id.video_stream_player)
        player = ExoPlayer.Builder(this).build()
        playerView.player = player
        playerView.useController = false
        val mediaItem = MediaItem.fromUri(videoUri[videoId])
        player.setMediaItem(mediaItem)
        player.prepare()
//        playerView.setOnTouchListener { _, event ->
//            gestureDetector.onTouchEvent(event)
//        }
        playerView.setOnClickListener {
            playerView.useController = true
        }
        player.playWhenReady = true
//        player.play()
    }

//    private fun initDetector(){
//        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
//            override fun onFling(
//                e1: MotionEvent,
//                e2: MotionEvent,
//                velocityX: Float,
//                velocityY: Float
//            ): Boolean {
//                // 监听向下滑动事件，并切换到下一个视频
//                if (velocityY < -100) {
//                    playVideo("https://files.lsmcloud.top/dog_and_cat_playing.mp4")
//                }
//                return super.onFling(e1, e2, velocityX, velocityY)
//            }
//        })
//    }

    private fun playVideo(uri: String) {
        player.stop()
        player.clearMediaItems()
        player.setMediaItem(MediaItem.fromUri(uri))
        player.prepare()
        player.play()
    }

    override fun onPause() {
        super.onPause()
        player.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}