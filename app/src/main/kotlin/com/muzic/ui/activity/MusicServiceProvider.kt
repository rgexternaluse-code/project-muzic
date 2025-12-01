package com.muzic.ui.activity

import com.muzic.service.MusicService

interface MusicServiceProvider {
    fun getMusicService(): MusicService?
}
