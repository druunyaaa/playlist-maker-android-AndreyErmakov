package com.example.playlistmarket.data

import com.example.playlistmarket.creator.Storage
import com.example.playlistmarket.data.dto.TracksSearchRequest
import com.example.playlistmarket.data.dto.TracksSearchResponse
import com.example.playlistmarket.domain.api.NetworkClient
import com.example.playlistmarket.data.dto.BaseResponse

class RetrofitNetworkClient(private val storage: Storage) : NetworkClient {

    override fun doRequest(request: Any): BaseResponse {
        if (request is TracksSearchRequest) {
            val searchList = storage.search(request.expression)
            return TracksSearchResponse(searchList).apply { resultCode = 200 }
        } else {
            return BaseResponse().apply { resultCode = 400 }
        }
    }
}