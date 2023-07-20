package com.liuyaoli.myapplication

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import androidx.media3.exoplayer.ExoPlayer


class VideoStreamActivity : AppCompatActivity() {
    private lateinit var player:  ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_stream_page)
        val backButton : ImageButton = findViewById(R.id.video_stream_back_button)
        backButton.setOnClickListener {
            finish()
        }
        playVideoStream()
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
        playerView.setOnTouchListener { _, _ ->
            playerView.useController = true
            false
        }
        player.playWhenReady = true
//        player.play()
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