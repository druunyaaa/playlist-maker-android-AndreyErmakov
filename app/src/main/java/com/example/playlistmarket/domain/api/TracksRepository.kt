package com.example.playlistmarket.domain.api

import com.example.playlistmarket.domain.models.Track

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
}