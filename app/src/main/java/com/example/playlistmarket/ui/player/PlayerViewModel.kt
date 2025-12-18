package com.example.playlistmarket.ui.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.App
import com.example.playlistmarket.data.PlaylistsRepositoryImpl
import com.example.playlistmarket.data.TracksRepositoryImpl
import com.example.playlistmarket.data.db.converters.DbConverter
import com.example.playlistmarket.data.network.RetrofitNetworkClient
import com.example.playlistmarket.domain.api.PlaylistsRepository
import com.example.playlistmarket.domain.api.TracksRepository
import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    // Инициализация реальных зависимостей
    private val app = application as App
    private val database = app.database
    private val historyPreferences = app.searchHistoryPreferences
    private val networkClient = RetrofitNetworkClient(application)
    private val converter = DbConverter()

    private val playlistsRepository: PlaylistsRepository = PlaylistsRepositoryImpl(database, converter)
    private val tracksRepository: TracksRepository = TracksRepositoryImpl(networkClient, database, historyPreferences)

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()

    fun checkFavoriteStatus(track: Track) {
        viewModelScope.launch {
            tracksRepository.getFavoriteTracks().collect { favorites ->
                val isTrackFavorite = favorites.any { it.id == track.id }
                _isFavorite.value = isTrackFavorite
            }
        }
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            val currentStatus = _isFavorite.value
            val newStatus = !currentStatus
            _isFavorite.value = newStatus
            tracksRepository.updateTrackFavoriteStatus(track, newStatus)
        }
    }

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            playlistsRepository.addTrackToPlaylist(track, playlist)
        }
    }

    companion object {
        fun getViewModelFactory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T = PlayerViewModel(application) as T
            }
    }
}