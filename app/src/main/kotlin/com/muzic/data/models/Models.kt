package com.muzic.data.models

import android.graphics.Bitmap

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val path: String,
    val albumArtPath: String? = null,
    val albumArt: Bitmap? = null,
    val folder: String = "Unknown"
)

data class Album(
    val id: Long,
    val title: String,
    val artist: String,
    val albumArt: Bitmap? = null,
    val songs: List<Song> = emptyList()
)

data class PlaybackState(
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val isLoading: Boolean = false,
    val playlistIndex: Int = 0,
    val playlist: List<Song> = emptyList()
)

enum class SortOption {
    ALPHABETICAL,
    BY_FOLDER,
    BY_ARTIST
}
