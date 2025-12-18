package com.example.playlistmarket.ui.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.data.DatabaseMock
import com.example.playlistmarket.data.PlaylistsRepositoryImpl
import com.example.playlistmarket.data.TracksRepositoryImpl
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

    private val database = DatabaseMock(viewModelScope)
    private val networkClient = RetrofitNetworkClient(application)
    private val playlistsRepository: PlaylistsRepository = PlaylistsRepositoryImpl(database)
    private val tracksRepository: TracksRepository = TracksRepositoryImpl(networkClient, database)

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    // Восстанавливаем получение плейлистов
    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()

    // Метод для проверки реального статуса лайка в БД (решает проблему 1)
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

    // Восстанавливаем добавление трека в плейлист
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