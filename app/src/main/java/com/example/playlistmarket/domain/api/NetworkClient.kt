package com.example.playlistmarket.domain.api

import com.example.playlistmarket.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}