package com.example.playlistmarket.ui.search

import com.example.playlistmarket.domain.models.Track

sealed interface SearchState {
    object Initial : SearchState
    object Searching : SearchState
    data class Success(val list: List<Track>) : SearchState
    data class Fail(val message: String) : SearchState
    data class History(val queries: List<String>) : SearchState
}