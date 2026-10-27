package com.autolink.music

import android.content.ComponentName
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.autolink.music.databinding.ActivityPlaybackBinding
import com.autolink.music.shared.PlaybackService
import com.google.common.util.concurrent.MoreExecutors

class PlaybackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaybackBinding
    private var controller: MediaController? = null

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            binding.status.setText(if (isPlaying) R.string.status_playing else R.string.status_paused)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaybackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.play.setOnClickListener { controller?.play() }
        binding.pause.setOnClickListener { controller?.pause() }
    }

    override fun onStart() {
        super.onStart()
        val token = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val future = MediaController.Builder(this, token).buildAsync()
        future.addListener({
            controller = future.get()
            controller?.addListener(playerListener)
        }, MoreExecutors.directExecutor())
    }

    override fun onStop() {
        controller?.removeListener(playerListener)
        controller?.release()
        controller = null
        super.onStop()
    }
}
