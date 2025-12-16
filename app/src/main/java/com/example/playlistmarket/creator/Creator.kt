package com.example.playlistmarket.creator

import com.example.playlistmarket.data.RetrofitNetworkClient
import com.example.playlistmarket.data.TracksRepositoryImpl
import com.example.playlistmarket.domain.api.TracksRepository

object Creator {
    fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(Storage()))
    }
}