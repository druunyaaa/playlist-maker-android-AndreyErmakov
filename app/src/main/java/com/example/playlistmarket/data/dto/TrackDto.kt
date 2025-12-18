package com.example.playlistmarket.data.dto

import com.google.gson.annotations.SerializedName

data class TrackDto(
    @SerializedName("trackId")
    val id: Long, // Аннотация обязательна, иначе id всегда будет 0
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Long,
    @SerializedName("artworkUrl100")
    val artworkUrl100: String,
    // Поле для локального хранения состояния лайка
    var isFavorite: Boolean = false
)