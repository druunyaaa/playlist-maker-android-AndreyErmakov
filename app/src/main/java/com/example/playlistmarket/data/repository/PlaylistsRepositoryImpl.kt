package com.example.playlistmarket.data.repository

import com.example.playlistmarket.data.db.AppDatabase
import com.example.playlistmarket.data.db.converters.DbConverter
import com.example.playlistmarket.data.db.entity.PlaylistEntity
import com.example.playlistmarket.domain.api.PlaylistsRepository
import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val database: AppDatabase,
    private val converter: DbConverter
) : PlaylistsRepository {

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return database.playlistDao().getPlaylists().map { entities ->
            entities.map { entity ->
                Playlist(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    coverImageUri = entity.coverImageUri,
                    tracks = converter.toTracksList(entity.tracksJson)
                )
            }
        }
    }

    override suspend fun createPlaylist(name: String, description: String, coverImageUri: String?) {
        val playlistEntity = PlaylistEntity(
            name = name,
            description = description,
            coverImageUri = coverImageUri,
            tracksJson = converter.fromTracksList(emptyList())
        )
        database.playlistDao().insertPlaylist(playlistEntity)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        val currentPlaylistEntity = database.playlistDao().getPlaylistByIdSuspend(playlist.id)
        if (currentPlaylistEntity != null) {
            val currentTracks = converter.toTracksList(currentPlaylistEntity.tracksJson).toMutableList()

            if (currentTracks.none { it.id == track.id }) {
                currentTracks.add(track)
                val updatedEntity = currentPlaylistEntity.copy(
                    tracksJson = converter.fromTracksList(currentTracks)
                )
                database.playlistDao().updatePlaylist(updatedEntity)
            }
        }
    }

    override fun getPlaylist(id: Long): Flow<Playlist?> {
        return database.playlistDao().getPlaylistById(id).map { entity ->
            entity?.let {
                Playlist(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    coverImageUri = it.coverImageUri,
                    tracks = converter.toTracksList(it.tracksJson)
                )
            }
        }
    }

    override suspend fun deletePlaylistById(id: Long) {
        database.playlistDao().deletePlaylistById(id)
    }
}