package com.example.playlistmarket.data

import com.example.playlistmarket.data.dto.TrackDto
import com.example.playlistmarket.data.dto.TracksSearchRequest
import com.example.playlistmarket.data.dto.TracksSearchResponse
import com.example.playlistmarket.domain.api.NetworkClient
import com.example.playlistmarket.domain.api.Resource
import com.example.playlistmarket.domain.api.TracksRepository
import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: DatabaseMock
) : TracksRepository {

    private fun mapDtoToDomain(dto: TrackDto): Track {
        return Track(
            id = dto.id,
            trackName = dto.trackName,
            artistName = dto.artistName,
            trackTimeMillis = dto.trackTimeMillis,
            artworkUrl100 = dto.artworkUrl100,
            favorite = dto.isFavorite
        )
    }

    // ИСПРАВЛЕНИЕ: Маппер из Domain в DTO для сохранения
    private fun mapDomainToDto(track: Track): TrackDto {
        return TrackDto(
            id = track.id,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            isFavorite = track.favorite
        )
    }

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }
            200 -> {
                val results = (response as TracksSearchResponse).results.map { mapDtoToDomain(it) }
                if (results.isEmpty()) {
                    emit(Resource.Success(emptyList()))
                } else {
                    emit(Resource.Success(results))
                }
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override fun getSearchQueryHistory(): List<String> {
        return database.getSearchQueryHistory()
    }

    override fun addSearchQuery(query: String) {
        database.addSearchQuery(query)
    }

    override fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        val trackDto = mapDomainToDto(track)
        database.updateTrackFavoriteStatus(trackDto, isFavorite)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return database.getFavoriteTracksFlow().map { list ->
            list.map { mapDtoToDomain(it) }
        }
    }
}