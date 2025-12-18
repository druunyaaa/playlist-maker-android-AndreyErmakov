package com.example.playlistmarket.ui.library

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
import kotlinx.coroutines.launch

class PlaylistsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as App
    private val database = app.database
    private val historyPreferences = app.searchHistoryPreferences
    private val networkClient = RetrofitNetworkClient(application)
    private val converter = DbConverter()

    private val playlistsRepository: PlaylistsRepository = PlaylistsRepositoryImpl(database, converter)
    private val tracksRepository: TracksRepository = TracksRepositoryImpl(networkClient, database, historyPreferences)

    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()
    val favoriteTracks: Flow<List<Track>> = tracksRepository.getFavoriteTracks()

    fun createPlaylist(name: String, description: String) {
        viewModelScope.launch {
            playlistsRepository.createPlaylist(name, description)
        }
    }

    companion object {
        fun getViewModelFactory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlaylistsViewModel(application) as T
                }
            }
    }
}