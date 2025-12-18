package com.example.playlistmarket.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmarket.data.DatabaseMock
import com.example.playlistmarket.data.PlaylistsRepositoryImpl
import com.example.playlistmarket.domain.api.PlaylistsRepository
import com.example.playlistmarket.domain.models.Playlist
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow

class PlaylistViewModel(
    private val repository: PlaylistsRepository,
    private val playlistId: Long
) : ViewModel() {

    val playlist: Flow<Playlist?> = repository.getPlaylist(playlistId)

    companion object {
        fun getViewModelFactory(playlistId: Long): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val database = DatabaseMock(GlobalScope)
                    val repository = PlaylistsRepositoryImpl(database)
                    return PlaylistViewModel(repository, playlistId) as T
                }
            }
    }
}