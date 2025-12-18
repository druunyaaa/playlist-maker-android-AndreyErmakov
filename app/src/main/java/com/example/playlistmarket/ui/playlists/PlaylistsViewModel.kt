package com.example.playlistmarket.ui.playlists

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.App
import com.example.playlistmarket.data.repository.PlaylistsRepositoryImpl
import com.example.playlistmarket.data.repository.TracksRepositoryImpl
import com.example.playlistmarket.data.db.converters.DbConverter
import com.example.playlistmarket.data.network.RetrofitNetworkClient
import com.example.playlistmarket.domain.api.PlaylistsRepository
import com.example.playlistmarket.domain.api.TracksRepository
import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PlaylistsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as App
    private val database = app.database
    private val historyPreferences = app.searchHistoryPreferences
    private val networkClient = RetrofitNetworkClient(application)
    private val converter = DbConverter()

    private val playlistsRepository: PlaylistsRepository = PlaylistsRepositoryImpl(database, converter)
    private val tracksRepository: TracksRepository = TracksRepositoryImpl(
        networkClient,
        database,
        historyPreferences,
        application
    )

    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()
    val favoriteTracks: Flow<List<Track>> = tracksRepository.getFavoriteTracks()

    private val _coverImageUri = MutableStateFlow<String?>(null)
    val coverImageUri = _coverImageUri.asStateFlow()

    fun setCoverImageUri(uri: String?) {
        _coverImageUri.value = uri
    }

    fun createPlaylist(name: String, description: String) {
        viewModelScope.launch {
            val tempUri = _coverImageUri.value
            var finalUri: String? = null

            if (tempUri != null) {
                finalUri = withContext(Dispatchers.IO) {
                    saveImageToPrivateStorage(Uri.parse(tempUri))
                }
            }

            playlistsRepository.createPlaylist(name, description, finalUri)
            _coverImageUri.value = null
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistsRepository.deletePlaylistById(playlist.id)
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri): String {
        val filePath = File(getApplication<Application>().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum_${System.currentTimeMillis()}.jpg")
        val inputStream = getApplication<Application>().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(filePath)
        BitmapFactory.decodeStream(inputStream).compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return filePath.toUri().toString()
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