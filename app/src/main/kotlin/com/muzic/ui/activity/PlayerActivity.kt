package com.muzic.ui.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.util.UnstableApi
import com.bumptech.glide.Glide
import com.muzic.R
import com.muzic.data.repository.MusicRepository
import com.muzic.databinding.ActivityPlayerBinding
import com.muzic.service.MusicService
import com.muzic.ui.viewmodel.PlayerViewModel
import com.muzic.utils.TimeFormatter
import com.muzic.utils.VisualizerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.timer

@UnstableApi
class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var musicRepository: MusicRepository
    private var musicService: MusicService? = null
    private var updateTimer: Timer? = null
    private var visualizerView: VisualizerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)
        musicRepository = MusicRepository(this)

        setupUI()
        startUpdateTimer()
    }

    private fun setupUI() {
        // Initialize visualizer
        visualizerView = VisualizerView(this)
        binding.visualizerContainer.addView(
            visualizerView,
            android.view.ViewGroup.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        // Play/Pause
        binding.btnPlayPause.setOnClickListener {
            if (musicService?.getPlayer()?.isPlaying == true) {
                musicService?.pause()
                binding.btnPlayPause.text = "⏸"
            } else {
                musicService?.play()
                binding.btnPlayPause.text = "⏯"
            }
        }

        // Next
        binding.btnNext.setOnClickListener {
            musicService?.skipNext()
        }

        // Previous
        binding.btnPrevious.setOnClickListener {
            musicService?.skipPrevious()
        }

        // Seek Forward
        binding.btnForward.setOnClickListener {
            musicService?.seekForward()
        }

        // Seek Backward
        binding.btnBackward.setOnClickListener {
            musicService?.seekBackward()
        }

        // Seekbar
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicService?.seekToPosition(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun startUpdateTimer() {
        updateTimer = timer(initialDelay = 0, period = 500) {
            updatePlayerUI()
        }
    }

    private fun updatePlayerUI() {
        musicService?.getPlayer()?.let { player ->
            val currentSong = musicService?.getCurrentSong()

            runOnUiThread {
                // Update song info
                currentSong?.let {
                    binding.playerSongTitle.text = it.title
                    binding.playerSongArtist.text = it.artist

                    // Update album art
                    if (it.albumArt != null) {
                        Glide.with(this)
                            .load(it.albumArt)
                            .placeholder(R.drawable.ic_cd_placeholder)
                            .into(binding.albumArtLarge)

                        // Extract dominant colors for gradient
                        val colors = musicRepository.extractDominantColors(it.albumArt)
                        colors?.let { (color1, color2) ->
                            applyGradientBackground(color1, color2)
                        }
                    } else {
                        Glide.with(this)
                            .load(R.drawable.ic_cd_placeholder)
                            .into(binding.albumArtLarge)
                    }
                }

                // Update playback controls
                binding.btnPlayPause.text = if (player.isPlaying) "⏯" else "⏸"

                // Update seekbar and times
                val duration = player.duration
                val currentPosition = player.currentPosition

                binding.seekBar.max = duration.toInt()
                binding.seekBar.progress = currentPosition.toInt()

                binding.currentTime.text = TimeFormatter.formatMillis(currentPosition)
                binding.totalDuration.text = TimeFormatter.formatMillis(duration)

                // Setup visualizer
                if (player.audioSessionId > 0) {
                    visualizerView?.setupVisualizer(player.audioSessionId)
                }
            }
        }
    }

    private fun applyGradientBackground(color1: Int, color2: Int) {
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(color1, color2)
        )
        binding.gradientBackground.background = gradient
    }

    override fun onStart() {
        super.onStart()
        // Get reference to service
        musicService = (application as? MusicServiceProvider)?.getMusicService()
    }

    override fun onDestroy() {
        super.onDestroy()
        updateTimer?.cancel()
        visualizerView?.release()
    }
}

// Interface to provide service reference (implement in Application class or use alternative)
interface MusicServiceProvider {
    fun getMusicService(): MusicService?
}
