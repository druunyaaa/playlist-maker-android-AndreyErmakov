package com.example.playlistmarket.domain.api

import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>

    fun getSearchQueryHistory(): List<String>
    fun addSearchQuery(query: String)

    fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean)
    fun getFavoriteTracks(): Flow<List<Track>>
}