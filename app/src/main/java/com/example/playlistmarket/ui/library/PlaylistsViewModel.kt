package com.example.playlistmarket.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.data.DatabaseMock
import com.example.playlistmarket.data.PlaylistsRepositoryImpl
import com.example.playlistmarket.data.TracksRepositoryImpl
import com.example.playlistmarket.domain.api.PlaylistsRepository
import com.example.playlistmarket.domain.api.TracksRepository
import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PlaylistsViewModel : ViewModel() {

    private val database = DatabaseMock(viewModelScope)

    private val playlistsRepository: PlaylistsRepository = PlaylistsRepositoryImpl(database)
    private val tracksRepository: TracksRepository = TracksRepositoryImpl(database)

    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()

    val favoriteTracks: Flow<List<Track>> = tracksRepository.getFavoriteTracks()
    fun createPlaylist(name: String, description: String) {
        viewModelScope.launch {
            playlistsRepository.createPlaylist(name, description)
        }
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlaylistsViewModel() as T
                }
            }
    }
}