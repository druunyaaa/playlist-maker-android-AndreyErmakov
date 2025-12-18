package com.example.playlistmarket.ui.playlists

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.ui.YsDisplay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistListItem(
    playlist: Playlist,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null
) {
    val tracksCountText = pluralStringResource(R.plurals.tracks_count, playlist.tracks.size, playlist.tracks.size)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (playlist.coverImageUri != null) {
            AsyncImage(
                model = Uri.parse(playlist.coverImageUri),
                contentDescription = playlist.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.default_playlist_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        }

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
    navigateToPlaylist: (Long) -> Unit
) {
    val playlists by playlistsViewModel.playlists.collectAsState(initial = emptyList())

    var playlistToDelete by remember { mutableStateOf<Playlist?>(null) }

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
                    text = stringResource(R.string.no_playlists),
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
                    PlaylistListItem(
                        playlist = playlists[index],
                        onClick = { navigateToPlaylist(playlists[index].id) },
                        onLongClick = {
                            playlistToDelete = playlists[index]
                        }
                    )
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
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.new_playlist_content_desc))
        }

        if (playlistToDelete != null) {
            AlertDialog(
                onDismissRequest = { playlistToDelete = null },
                containerColor = Color.White,
                title = { Text(text = stringResource(R.string.delete_playlist_title), fontFamily = YsDisplay, fontWeight = FontWeight.Medium) },
                text = { Text(text = stringResource(R.string.delete_playlist_message, playlistToDelete?.name ?: ""), fontFamily = YsDisplay) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            playlistToDelete?.let { playlistsViewModel.deletePlaylist(it) }
                            playlistToDelete = null
                        }
                    ) {
                        Text(stringResource(R.string.yes), color = Color(0xFF3772E7), fontFamily = YsDisplay)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { playlistToDelete = null }) {
                        Text(stringResource(R.string.no), color = Color(0xFF3772E7), fontFamily = YsDisplay)
                    }
                }
            )
        }
    }
}