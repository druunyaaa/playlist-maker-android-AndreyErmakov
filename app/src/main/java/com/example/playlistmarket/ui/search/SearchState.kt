package com.example.playlistmarket.ui.search

import com.example.playlistmarket.domain.models.Track

sealed class SearchState {
    object Initial : SearchState()
    object Searching : SearchState()
    data class Success(val list: List<Track>) : SearchState()
    data class Fail(val error: String) : SearchState()
}