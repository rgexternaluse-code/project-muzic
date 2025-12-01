package com.muzic.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.muzic.data.models.Song
import com.muzic.data.models.SortOption
import com.muzic.data.repository.MusicRepository
import kotlinx.coroutines.launch

class MusicListViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = MusicRepository(application)
    
    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> = _songs
    
    private val _sortOption = MutableLiveData(SortOption.ALPHABETICAL)
    val sortOption: LiveData<SortOption> = _sortOption
    
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    init {
        loadSongs()
    }
    
    fun loadSongs() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allSongs = repository.getAllSongs()
                val sortedSongs = repository.sortSongs(allSongs, _sortOption.value ?: SortOption.ALPHABETICAL)
                _songs.value = sortedSongs
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun setSortOption(option: SortOption) {
        _sortOption.value = option
        val currentSongs = _songs.value ?: return
        _songs.value = repository.sortSongs(currentSongs, option)
    }
}
