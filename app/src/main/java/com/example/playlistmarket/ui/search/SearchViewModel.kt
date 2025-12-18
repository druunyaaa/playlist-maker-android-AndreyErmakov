package com.example.playlistmarket.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.App
import com.example.playlistmarket.data.repository.TracksRepositoryImpl
import com.example.playlistmarket.data.network.RetrofitNetworkClient
import com.example.playlistmarket.domain.api.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val database = (application as App).database
    private val historyPreferences = (application as App).searchHistoryPreferences
    private val networkClient = RetrofitNetworkClient(application)

    private val repository = TracksRepositoryImpl(
        networkClient,
        database,
        historyPreferences,
        application
    )

    var lastQuery = ""

    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState = _searchScreenState.asStateFlow()

    private var searchJob: Job? = null

    init {
        showHistory()
    }

    fun search(whatSearch: String) {
        searchJob?.cancel()
        if (whatSearch.isEmpty()) {
            showHistory()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            performSearch(whatSearch, saveToHistory = true)
        }
    }

    fun saveSearchQuery(query: String) {
        if (query.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.addSearchQuery(query)
            }
        }
    }

    fun searchDebounced(whatSearch: String) {
        searchJob?.cancel()

        if (whatSearch.isEmpty()) {
            showHistory()
            return
        }

        _searchScreenState.update { SearchState.Searching }

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            performSearch(whatSearch, saveToHistory = false)
        }
    }



    private suspend fun performSearch(whatSearch: String, saveToHistory: Boolean) {
        if (saveToHistory) {
            repository.addSearchQuery(whatSearch)
        }

        repository.searchTracks(whatSearch).collect { result ->
            when (result) {
                is Resource.Success -> {
                    val tracks = result.data
                    if (tracks.isNotEmpty()) {
                        _searchScreenState.update { SearchState.Success(tracks) }
                    } else {
                        _searchScreenState.update { SearchState.Success(emptyList()) }
                    }
                }
                is Resource.Error -> {
                    _searchScreenState.update { SearchState.Fail(result.message) }
                }
            }
        }
    }

    fun showHistory() {
        searchJob?.cancel()
        viewModelScope.launch {
            val queries = repository.getSearchQueryHistory()
            if (queries.isNotEmpty()) {
                _searchScreenState.update { SearchState.History(queries) }
            } else {
                _searchScreenState.update { SearchState.Initial }
            }
        }
    }

    fun clearSearchText() {
        searchJob?.cancel()
        lastQuery = ""
        showHistory()
    }

    companion object {
        fun getViewModelFactory(application: Application): ViewModelProvider.Factory =
            androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }
}