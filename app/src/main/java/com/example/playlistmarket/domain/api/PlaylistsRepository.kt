package com.example.playlistmarket.domain.api

import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun createPlaylist(name: String, description: String)

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)

    fun getPlaylist(id: Long): Flow<Playlist?>

    suspend fun deletePlaylistById(id: Long)
}