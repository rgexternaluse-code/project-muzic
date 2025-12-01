package com.muzic.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.muzic.data.models.Song
import com.muzic.ui.activity.PlayerActivity

@UnstableApi
class MusicService : Service() {

    private lateinit var exoPlayer: ExoPlayer
    private var mediaSession: MediaSession? = null
    private lateinit var notificationManager: NotificationManager
    
    private val binder = LocalBinder()
    private var currentPlaylist: List<Song> = emptyList()
    
    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "muzic_playback_channel"
    }
    
    inner class LocalBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }
    
    override fun onCreate() {
        super.onCreate()
        setupExoPlayer()
        setupNotificationChannel()
    }
    
    private fun setupExoPlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                updateNotification()
            }
            
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if (isPlaying) {
                    startForeground(NOTIFICATION_ID, buildNotification())
                } else {
                    stopForeground(STOP_FOREGROUND_REMOVE)
                }
            }
        })
        
        mediaSession = MediaSession.Builder(this, exoPlayer).build()
    }
    
    private fun setupNotificationChannel() {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    fun loadPlaylist(songs: List<Song>) {
        currentPlaylist = songs
        val mediaItems = songs.map { song ->
            MediaItem.Builder()
                .setUri(song.path)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        .setAlbumTitle(song.album)
                        .build()
                )
                .build()
        }
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
    }
    
    fun play() {
        exoPlayer.play()
    }
    
    fun pause() {
        exoPlayer.pause()
    }
    
    fun seekToPosition(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
    }
    
    fun skipNext() {
        if (exoPlayer.hasNextMediaItem()) {
            exoPlayer.seekToNextMediaItem()
        }
    }
    
    fun skipPrevious() {
        if (exoPlayer.hasPreviousMediaItem()) {
            exoPlayer.seekToPreviousMediaItem()
        }
    }
    
    fun seekForward() {
        val currentPosition = exoPlayer.currentPosition
        exoPlayer.seekTo(currentPosition + 10000) // 10 seconds
    }
    
    fun seekBackward() {
        val currentPosition = exoPlayer.currentPosition
        val newPosition = (currentPosition - 10000).coerceAtLeast(0)
        exoPlayer.seekTo(newPosition)
    }
    
    fun getPlayer(): ExoPlayer = exoPlayer
    
    fun getCurrentSong(): Song? {
        val index = exoPlayer.currentMediaItemIndex
        return if (index in currentPlaylist.indices) currentPlaylist[index] else null
    }
    
    private fun buildNotification(): Notification {
        val currentSong = getCurrentSong()
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, PlayerActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(currentSong?.title ?: "Now Playing")
            .setContentText(currentSong?.artist ?: "Unknown Artist")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingIntent)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession?.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .addAction(
                android.R.drawable.ic_media_previous,
                "Previous",
                createPendingIntent("PREVIOUS")
            )
            .addAction(
                if (exoPlayer.isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
                if (exoPlayer.isPlaying) "Pause" else "Play",
                createPendingIntent("PLAY_PAUSE")
            )
            .addAction(
                android.R.drawable.ic_media_next,
                "Next",
                createPendingIntent("NEXT")
            )
            .setAutoCancel(false)
            .build()
    }
    
    private fun updateNotification() {
        notificationManager.notify(NOTIFICATION_ID, buildNotification())
    }
    
    private fun createPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this, action.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY_PAUSE" -> {
                if (exoPlayer.isPlaying) pause() else play()
            }
            "NEXT" -> skipNext()
            "PREVIOUS" -> skipPrevious()
        }
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
    
    override fun onDestroy() {
        mediaSession?.release()
        exoPlayer.release()
        super.onDestroy()
    }
}
