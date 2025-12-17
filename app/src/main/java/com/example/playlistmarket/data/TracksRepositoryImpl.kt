package com.example.playlistmarket.data

import com.example.playlistmarket.data.dto.TrackDto
import com.example.playlistmarket.domain.api.Resource
import com.example.playlistmarket.domain.api.TracksRepository
import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
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

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        try {
            val response = database.searchTracks(expression)
            val data = response.map { mapDtoToDomain(it) }

            if (data.isEmpty()) {
                emit(Resource.Success(emptyList()))
            } else {
                emit(Resource.Success(data))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Ошибка сервера"))
        }
    }

    override fun getSearchQueryHistory(): List<String> {
        return database.getSearchQueryHistory()
    }

    override fun addSearchQuery(query: String) {
        database.addSearchQuery(query)
    }

    override fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        database.updateTrackFavoriteStatus(track.id, isFavorite)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val dtos = database.getFavoriteTracksDto()
        val tracks = dtos.map { mapDtoToDomain(it) }
        emit(tracks)
    }
}