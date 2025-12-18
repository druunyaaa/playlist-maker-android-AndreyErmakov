package com.example.playlistmarket.domain.api

import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>

    suspend fun getSearchQueryHistory(): List<String>

    suspend fun addSearchQuery(query: String)

    suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean)

    fun getFavoriteTracks(): Flow<List<Track>>
}