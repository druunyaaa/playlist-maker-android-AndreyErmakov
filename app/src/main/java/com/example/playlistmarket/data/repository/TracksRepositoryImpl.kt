package com.example.playlistmarket.data.repository

import android.content.Context
import com.example.playlistmarket.R
import com.example.playlistmarket.data.db.AppDatabase
import com.example.playlistmarket.data.db.entity.TrackEntity
import com.example.playlistmarket.data.dto.TrackDto
import com.example.playlistmarket.data.dto.TracksSearchRequest
import com.example.playlistmarket.data.dto.TracksSearchResponse
import com.example.playlistmarket.data.preferences.SearchHistoryPreferences
import com.example.playlistmarket.domain.api.NetworkClient
import com.example.playlistmarket.domain.api.Resource
import com.example.playlistmarket.domain.api.TracksRepository
import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: AppDatabase,
    private val historyPreferences: SearchHistoryPreferences,
    private val context: Context
) : TracksRepository {

    private fun mapDtoToDomain(dto: TrackDto, isFavorite: Boolean): Track {
        return Track(
            id = dto.id,
            trackName = dto.trackName,
            artistName = dto.artistName,
            trackTimeMillis = dto.trackTimeMillis,
            artworkUrl100 = dto.artworkUrl100,
            favorite = isFavorite
        )
    }

    private fun mapDomainToEntity(track: Track): TrackEntity {
        return TrackEntity(
            id = track.id,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            addedTimestamp = System.currentTimeMillis()
        )
    }

    private fun mapEntityToDomain(entity: TrackEntity): Track {
        return Track(
            id = entity.id,
            trackName = entity.trackName,
            artistName = entity.artistName,
            trackTimeMillis = entity.trackTimeMillis,
            artworkUrl100 = entity.artworkUrl100,
            favorite = true
        )
    }

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(context.getString(R.string.connection_error)))
            }
            200 -> {
                val favoriteIds = database.trackDao().getFavoriteTrackIds()

                val results = (response as TracksSearchResponse).results.map { dto ->
                    mapDtoToDomain(dto, isFavorite = favoriteIds.contains(dto.id))
                }

                if (results.isEmpty()) {
                    emit(Resource.Success(emptyList()))
                } else {
                    emit(Resource.Success(results))
                }
            }
            else -> {
                emit(Resource.Error(context.getString(R.string.server_error)))
            }
        }
    }

    override suspend fun getSearchQueryHistory(): List<String> {
        return historyPreferences.getEntries()
    }

    override suspend fun addSearchQuery(query: String) {
        historyPreferences.addEntry(query)
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        if (isFavorite) {
            database.trackDao().insertTrack(mapDomainToEntity(track))
        } else {
            database.trackDao().deleteTrack(mapDomainToEntity(track))
        }
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return database.trackDao().getFavoriteTracks().map { list ->
            list.map { mapEntityToDomain(it) }
        }
    }
}