package com.example.playlistmarket.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmarket.data.dto.TracksSearchRequest
import com.example.playlistmarket.data.dto.BaseResponse
import com.example.playlistmarket.domain.api.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val imdbBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    override fun doRequest(dto: Any): BaseResponse {
        if (!isConnected()) {
            return BaseResponse().apply { resultCode = -1 }
        }

        if (dto !is TracksSearchRequest) {
            return BaseResponse().apply { resultCode = 400 }
        }

        return try {
            val resp = iTunesService.search(dto.expression).execute()
            val body = resp.body() ?: BaseResponse()
            body.apply { resultCode = resp.code() }
        } catch (e: Exception) {
            e.printStackTrace() // Это выведет реальную ошибку (Timeout, DNS, и т.д.) в консоль
            BaseResponse().apply { resultCode = 500 }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}