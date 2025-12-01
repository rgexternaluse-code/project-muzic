package com.muzic.data.repository

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import androidx.palette.graphics.Palette
import com.muzic.data.models.Song
import com.muzic.data.models.SortOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MusicRepository(private val context: Context) {

    private val supportedFormats = setOf("mp3", "wav", "m4a", "aac", "flac")

    suspend fun getAllSongs(): List<Song> = withContext(Dispatchers.IO) {
        val songs = mutableListOf<Song>()
        val contentResolver = context.contentResolver
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val cursor: Cursor? = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            "${MediaStore.Audio.Media.IS_MUSIC} != 0",
            null,
            null
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val pathColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (it.moveToNext()) {
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn)
                val album = it.getString(albumColumn)
                val duration = it.getLong(durationColumn)
                val path = it.getString(pathColumn)
                val id = it.getLong(idColumn)
                val albumId = it.getLong(albumIdColumn)
                val folder = File(path).parentFile?.name ?: "Unknown"

                // Check if file format is supported
                val extension = File(path).extension.lowercase()
                if (extension in supportedFormats && duration > 0) {
                    val albumArt = getAlbumArt(albumId)
                    songs.add(
                        Song(
                            id = id,
                            title = title,
                            artist = artist,
                            album = album,
                            duration = duration,
                            path = path,
                            albumArt = albumArt,
                            folder = folder
                        )
                    )
                }
            }
        }

        songs
    }

    fun sortSongs(songs: List<Song>, sortOption: SortOption): List<Song> {
        return when (sortOption) {
            SortOption.ALPHABETICAL -> songs.sortedBy { it.title }
            SortOption.BY_FOLDER -> songs.sortedWith(compareBy({ it.folder }, { it.title }))
            SortOption.BY_ARTIST -> songs.sortedWith(compareBy({ it.artist }, { it.title }))
        }
    }

    private fun getAlbumArt(albumId: Long): Bitmap? {
        return try {
            val artworkUri = android.net.Uri.parse(
                "content://media/external/audio/albumart/$albumId"
            )
            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(artworkUri)
            )
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    fun extractDominantColors(bitmap: Bitmap): Pair<Int, Int>? {
        return try {
            val palette = Palette.from(bitmap).generate()
            val vibrant = palette.getVibrantColor(0)
            val darkVibrant = palette.getDarkVibrantColor(0)
            Pair(vibrant, darkVibrant)
        } catch (e: Exception) {
            null
        }
    }
}
