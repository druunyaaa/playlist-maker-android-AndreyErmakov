package com.example.playlistmarket.domain.api

sealed interface Resource<T> {
    data class Success<T>(val data: T) : Resource<T>
    data class Error<T>(val message: String, val data: T? = null) : Resource<T>
}