package com.autolink.music.shared

import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession

class PlaybackService : MediaLibraryService() {
    private lateinit var player: ExoPlayer
    private lateinit var session: MediaLibrarySession

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .setHandleAudioBecomingNoisy(true)
            .build()
        player.setMediaItem(MediaItem.fromUri(SAMPLE_URI))
        player.prepare()
        session = MediaLibrarySession.Builder(this, player, LibraryCallback()).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession = session

    override fun onDestroy() {
        session.release()
        player.release()
        super.onDestroy()
    }

    private class LibraryCallback : MediaLibrarySession.Callback

    private companion object {
        const val SAMPLE_URI = "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"
    }
}
