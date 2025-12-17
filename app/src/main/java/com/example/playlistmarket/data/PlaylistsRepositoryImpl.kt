package com.example.playlistmarket.data

import com.example.playlistmarket.domain.api.PlaylistsRepository
import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsRepositoryImpl(
    private val database: DatabaseMock
) : PlaylistsRepository {

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return database.getAllPlaylists()
    }

    override fun createPlaylist(name: String, description: String) {
        database.addNewPlaylist(name, description)
    }

    override fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        database.addTrackToPlaylist(track, playlist)
    }

    override fun getPlaylist(id: Long): Flow<Playlist?> {
        return database.getPlaylist(id)
    }

    override fun deletePlaylistById(id: Long) {
        database.deletePlaylistById(id)
    }
}