package com.muzic.ui.activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.popupmenu.PopupMenu
import com.muzic.R
import com.muzic.data.models.SortOption
import com.muzic.databinding.ActivityMainBinding
import com.muzic.service.MusicService
import com.muzic.ui.adapter.SongAdapter
import com.muzic.ui.viewmodel.MusicListViewModel
import com.muzic.ui.viewmodel.PlayerViewModel

@UnstableApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var musicListViewModel: MusicListViewModel
    private lateinit var playerViewModel: PlayerViewModel
    private var musicService: MusicService? = null
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.LocalBinder
            musicService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            musicService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        musicListViewModel = ViewModelProvider(this).get(MusicListViewModel::class.java)
        playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

        setupUI()
        checkPermissions()
        bindMusicService()
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbar)

        binding.sortButton.setOnClickListener {
            showSortMenu()
        }

        musicListViewModel.songs.observe(this) { songs ->
            val adapter = SongAdapter(songs) { song, index ->
                musicService?.loadPlaylist(songs)
                musicService?.getPlayer()?.seekToIndex(index)
                musicService?.play()
                startPlayerActivity(songs, index)
            }
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                this.adapter = adapter
            }
        }

        musicListViewModel.isLoading.observe(this) { isLoading ->
            binding.loadingSpinner.apply {
                visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
            }
        }
    }

    private fun showSortMenu() {
        val popup = PopupMenu(this, binding.sortButton)
        popup.menuInflater.inflate(R.menu.sort_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.sort_alphabetical -> {
                    musicListViewModel.setSortOption(SortOption.ALPHABETICAL)
                    true
                }
                R.id.sort_folder -> {
                    musicListViewModel.setSortOption(SortOption.BY_FOLDER)
                    true
                }
                R.id.sort_artist -> {
                    musicListViewModel.setSortOption(SortOption.BY_ARTIST)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun startPlayerActivity(songs: List<Any>, index: Int) {
        val intent = Intent(this, PlayerActivity::class.java)
        startActivity(intent)
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
                    100
                )
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    100
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            musicListViewModel.loadSongs()
        }
    }

    private fun bindMusicService() {
        Intent(this, MusicService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            startService(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(serviceConnection)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}
