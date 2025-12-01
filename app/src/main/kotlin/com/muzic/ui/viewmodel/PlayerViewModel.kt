package com.muzic.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.muzic.data.models.PlaybackState
import com.muzic.data.models.Song

class PlayerViewModel(application: Application) : AndroidViewModel(application) {
    
    private val _playbackState = MutableLiveData<PlaybackState>(PlaybackState())
    val playbackState: LiveData<PlaybackState> = _playbackState
    
    private val _currentSongIndex = MutableLiveData(0)
    val currentSongIndex: LiveData<Int> = _currentSongIndex
    
    private val _dominantColors = MutableLiveData<Pair<Int, Int>?>(null)
    val dominantColors: LiveData<Pair<Int, Int>?> = _dominantColors
    
    fun updatePlaybackState(state: PlaybackState) {
        _playbackState.value = state
    }
    
    fun setCurrentSongIndex(index: Int) {
        _currentSongIndex.value = index
    }
    
    fun setDominantColors(colors: Pair<Int, Int>?) {
        _dominantColors.value = colors
    }
}
