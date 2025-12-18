package com.example.playlistmarket.ui.library

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmarket.App
import com.example.playlistmarket.data.PlaylistsRepositoryImpl
import com.example.playlistmarket.data.db.converters.DbConverter
import com.example.playlistmarket.domain.api.PlaylistsRepository
import com.example.playlistmarket.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistViewModel(
    private val repository: PlaylistsRepository,
    private val playlistId: Long
) : ViewModel() {

    val playlist: Flow<Playlist?> = repository.getPlaylist(playlistId)

    companion object {
        // Добавили application в параметры фабрики
        fun getViewModelFactory(playlistId: Long, application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val app = application as App
                    val database = app.database
                    val converter = DbConverter()
                    val repository = PlaylistsRepositoryImpl(database, converter)

                    return PlaylistViewModel(repository, playlistId) as T
                }
            }
    }
}