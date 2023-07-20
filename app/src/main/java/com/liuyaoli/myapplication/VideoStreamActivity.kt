package com.liuyaoli.myapplication

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import androidx.media3.exoplayer.ExoPlayer


class VideoStreamActivity : AppCompatActivity() {
    private lateinit var player:  ExoPlayer
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_stream_page)
        val backButton : ImageButton = findViewById(R.id.video_stream_back_button)
        backButton.setOnClickListener {
            finish()
        }
        playVideoStream()
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                // 监听向下滑动事件，并切换到下一个视频
                if (velocityY < -100) {
                    playVideo("https://files.lsmcloud.top/dog_and_cat_playing.mp4")
                }
                return super.onFling(e1, e2, velocityX, velocityY)
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun playVideoStream(){
        val playerView = findViewById<PlayerView>(R.id.video_stream_player)
        player = ExoPlayer.Builder(this).build()
        playerView.player = player
        playerView.useController = false
        val mediaItem = MediaItem.fromUri("https://files.lsmcloud.top/xyq_playing.mp4")
        player.setMediaItem(mediaItem)
        player.prepare()
        playerView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
        playerView.setOnClickListener {
            playerView.useController = true
        }
        player.playWhenReady = true
//        player.play()
    }

    private fun playVideo(uri:String){
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