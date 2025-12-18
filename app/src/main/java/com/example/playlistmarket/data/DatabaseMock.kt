package com.example.playlistmarket.data

import com.example.playlistmarket.data.dto.TrackDto
import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.domain.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class DatabaseMock(
    private val scope: CoroutineScope,
) {
    companion object {
        private val searchQueryHistory = mutableListOf<String>()
        private val _playlistsFlow = MutableStateFlow<List<Playlist>>(emptyList())

        private const val MOCK_ARTWORK = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/190295389877.jpg/100x100bb.jpg"

        private val tracksDto = mutableListOf(
            TrackDto(1, "Владивосток 2000", "Мумий Троль", 158000, MOCK_ARTWORK),
            TrackDto(2, "Группа крови", "Кино", 283000, MOCK_ARTWORK),
            TrackDto(3, "Не смотри назад", "Ария", 312000, MOCK_ARTWORK),
            TrackDto(4, "Звезда по имени Солнце", "Кино", 225000, MOCK_ARTWORK),
            TrackDto(5, "Лондон", "Аквариум", 272000, MOCK_ARTWORK),
            TrackDto(6, "На заре", "Альянс", 230000, MOCK_ARTWORK),
            TrackDto(7, "Перемен", "Кино", 296000, MOCK_ARTWORK),
            TrackDto(8, "Розовый фламинго", "Сплин", 195000, MOCK_ARTWORK),
            TrackDto(9, "Танцевать", "Мельница", 222000, MOCK_ARTWORK),
            TrackDto(10, "Чёрный бумер", "Серега", 241000, MOCK_ARTWORK)
        )

        private val _tracksFlow = MutableStateFlow<List<TrackDto>>(tracksDto.toList())
    }

    fun getSearchQueryHistory(): List<String> = searchQueryHistory.toList()

    fun addSearchQuery(query: String) {
        if (query.isBlank()) return
        val existingIndex = searchQueryHistory.indexOfFirst { it.equals(query, ignoreCase = true) }
        if (existingIndex != -1) {
            searchQueryHistory.removeAt(existingIndex)
        }
        searchQueryHistory.add(0, query)
        if (searchQueryHistory.size > 10) searchQueryHistory.removeAt(searchQueryHistory.lastIndex)
    }

    fun searchTracks(expression: String): List<TrackDto> {
        if (expression.isEmpty()) return emptyList()
        return tracksDto.filter {
            it.trackName.contains(expression, true) || it.artistName.contains(expression, true)
        }
    }

    // ИСПРАВЛЕНИЕ: Метод принимает TrackDto, чтобы исправить Type mismatch в Repository
    fun updateTrackFavoriteStatus(trackDto: TrackDto, isFavorite: Boolean) {
        val index = tracksDto.indexOfFirst { it.id == trackDto.id }
        if (index != -1) {
            tracksDto[index] = tracksDto[index].copy(isFavorite = isFavorite)
        } else if (isFavorite) {
            // Если трека нет в базе (пришел из поиска), добавляем его
            tracksDto.add(trackDto.copy(isFavorite = true))
        } else {
            return
        }

        _tracksFlow.value = tracksDto.toList()

        // Обновляем треки внутри плейлистов
        _playlistsFlow.update { currentPlaylists ->
            currentPlaylists.map { playlist ->
                val updatedTracks = playlist.tracks.map {
                    if (it.id == trackDto.id) it.copy(favorite = isFavorite) else it
                }
                playlist.copy(tracks = updatedTracks)
            }
        }
    }

    fun getFavoriteTracksDto(): List<TrackDto> {
        return tracksDto.filter { it.isFavorite }
    }

    fun getFavoriteTracksFlow(): Flow<List<TrackDto>> {
        return _tracksFlow.map { list -> list.filter { it.isFavorite } }
    }

    fun getAllPlaylists(): Flow<List<Playlist>> {
        return _playlistsFlow.asStateFlow()
    }

    fun getPlaylist(id: Long): Flow<Playlist?> {
        return _playlistsFlow.map { playlists ->
            playlists.find { it.id == id }
        }
    }

    fun addNewPlaylist(name: String, description: String) {
        val newPlaylist = Playlist(System.currentTimeMillis(), name, description, emptyList())
        _playlistsFlow.update { it + newPlaylist }
    }

    fun deletePlaylistById(id: Long) {
        _playlistsFlow.update { list -> list.filter { it.id != id } }
    }

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        _playlistsFlow.update { currentPlaylists ->
            currentPlaylists.map { pl ->
                if (pl.id == playlist.id) {
                    // Логика верная, проблема с дублями была в TrackDto
                    if (pl.tracks.any { it.id == track.id }) pl else pl.copy(tracks = pl.tracks + track)
                } else {
                    pl
                }
            }
        }
    }
}