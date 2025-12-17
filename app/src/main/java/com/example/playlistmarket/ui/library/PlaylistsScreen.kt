package com.example.playlistmarket.ui.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.ui.YsDisplay

@Composable
fun PlaylistListItem(playlist: Playlist, onClick: () -> Unit) {
    val tracksCountText = getPlaylistPluralString(playlist.tracks.size, "трек", "трека", "треков")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick() })
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.default_playlist_icon),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = playlist.name,
                fontSize = 16.sp,
                fontFamily = YsDisplay,
                color = Color.Black
            )
            Text(
                text = tracksCountText,
                fontSize = 12.sp,
                fontFamily = YsDisplay,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun PlaylistsScreen(
    playlistsViewModel: PlaylistsViewModel,
    addNewPlaylist: () -> Unit,
    navigateBack: () -> Unit,
    navigateToPlaylist: (Long) -> Unit
) {
    val playlists by playlistsViewModel.playlists.collectAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        if (playlists.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nothing_found_icon),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
                Text(
                    text = "Плейлистов еще нет",
                    fontSize = 19.sp,
                    fontFamily = YsDisplay,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                items(playlists.size) { index ->
                    PlaylistListItem(playlist = playlists[index]) {
                        navigateToPlaylist(playlists[index].id)
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = addNewPlaylist,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .navigationBarsPadding(),
            containerColor = Color(0xFF3772E7),
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "New Playlist")
        }
    }
}

fun getPlaylistPluralString(count: Int, one: String, two: String, five: String): String {
    val n = count % 100
    val n1 = n % 10

    val word = when {
        n in 11..19 -> five
        n1 == 1 -> one
        n1 in 2..4 -> two
        else -> five
    }
    return "$count $word"
}