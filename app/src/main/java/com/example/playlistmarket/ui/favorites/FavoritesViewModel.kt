package com.example.playlistmarket.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.App
import com.example.playlistmarket.data.repository.TracksRepositoryImpl
import com.example.playlistmarket.data.network.RetrofitNetworkClient
import com.example.playlistmarket.domain.api.TracksRepository
import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as App
    private val tracksRepository: TracksRepository = TracksRepositoryImpl(
        RetrofitNetworkClient(application),
        app.database,
        app.searchHistoryPreferences,
        application
    )

    val favoriteTracks: Flow<List<Track>> = tracksRepository.getFavoriteTracks()

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            tracksRepository.updateTrackFavoriteStatus(track, false)
        }
    }

    companion object {
        fun getViewModelFactory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FavoritesViewModel(application) as T
                }
            }
    }
}