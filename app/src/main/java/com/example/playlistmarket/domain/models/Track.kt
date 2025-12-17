package com.example.playlistmarket.domain.models

data class Track(
    val id: Long = 0,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val favorite: Boolean = false
)