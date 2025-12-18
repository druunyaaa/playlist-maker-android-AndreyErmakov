package com.example.playlistmarket.data.db.converters

import androidx.room.TypeConverter
import com.example.playlistmarket.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DbConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromTracksList(tracks: List<Track>): String {
        return gson.toJson(tracks)
    }

    @TypeConverter
    fun toTracksList(value: String): List<Track> {
        val type = object : TypeToken<List<Track>>() {}.type
        return gson.fromJson(value, type)
    }
}