package com.example.playlistmarket.data

import com.example.playlistmarket.data.dto.TracksSearchRequest
import com.example.playlistmarket.data.dto.TracksSearchResponse
import com.example.playlistmarket.domain.api.NetworkClient
import com.example.playlistmarket.domain.api.TracksRepository
import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.delay

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        delay(1000L)

        return if (response.resultCode == 200) {
            (response as TracksSearchResponse).results.map {
                val seconds = it.trackTimeMillis / 1000
                val minutes = seconds / 60
                val trackTime = "%02d:%02d".format(minutes, seconds % 60)

                Track(it.trackName, it.artistName, trackTime)
            }
        } else {
            emptyList()
        }
    }
}