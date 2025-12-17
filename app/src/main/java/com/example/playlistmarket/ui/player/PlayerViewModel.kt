package com.example.playlistmarket.ui.player

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

class PlayerViewModel : ViewModel() {

    private val database = DatabaseMock(viewModelScope)

    private val playlistsRepository: PlaylistsRepository = PlaylistsRepositoryImpl(database)
    private val tracksRepository: TracksRepository = TracksRepositoryImpl(database)

    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            playlistsRepository.addTrackToPlaylist(track, playlist)
        }
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            val newStatus = !track.favorite
            tracksRepository.updateTrackFavoriteStatus(track, newStatus)
        }
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel() as T
                }
            }
    }
}