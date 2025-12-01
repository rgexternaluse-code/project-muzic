package com.muzic.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muzic.R
import com.muzic.data.models.Song
import com.muzic.databinding.ItemSongBinding
import java.util.concurrent.TimeUnit

class SongAdapter(
    private val songs: List<Song>,
    private val onSongClick: (Song, Int) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position], position)
    }

    override fun getItemCount(): Int = songs.size

    inner class SongViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song, position: Int) {
            binding.apply {
                songTitle.text = song.title
                songArtist.text = song.artist
                songDuration.text = formatDuration(song.duration)
                
                // Load album art or show default CD image
                if (song.albumArt != null) {
                    Glide.with(root)
                        .load(song.albumArt)
                        .placeholder(R.drawable.ic_cd_placeholder)
                        .error(R.drawable.ic_cd_placeholder)
                        .into(albumArtThumb)
                } else {
                    Glide.with(root)
                        .load(R.drawable.ic_cd_placeholder)
                        .into(albumArtThumb)
                }
                
                root.setOnClickListener {
                    onSongClick(song, position)
                }
            }
        }

        private fun formatDuration(durationMs: Long): String {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60
            return String.format("%02d:%02d", minutes, seconds)
        }
    }
}
