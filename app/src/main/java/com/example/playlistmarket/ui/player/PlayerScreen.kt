package com.example.playlistmarket.ui.player

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.ui.YsDisplay
import com.example.playlistmarket.ui.components.rememberClickDebouncer
import com.example.playlistmarket.ui.library.PlaylistListItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    track: Track,
    viewModel: PlayerViewModel,
    onBackClick: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val debouncer = rememberClickDebouncer()

    val playlists by viewModel.playlists.collectAsState(initial = emptyList())
    val isFavorite by viewModel.isFavorite.collectAsState()

    LaunchedEffect(track) {
        viewModel.checkFavoriteStatus(track)
    }

    val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { debouncer.click { onBackClick() } }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .padding(24.dp)
        ) {
            AsyncImage(
                model = track.artworkUrl100.replace("100x100bb.jpg", "512x512bb.jpg"),
                contentDescription = "Cover",
                placeholder = painterResource(id = R.drawable.music_icon),
                error = painterResource(id = R.drawable.music_icon),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                track.trackName,
                fontSize = 22.sp,
                fontFamily = YsDisplay,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                track.artistName,
                fontSize = 14.sp,
                fontFamily = YsDisplay,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(36.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showBottomSheet = true },
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color(0xFFF1F4FA), RoundedCornerShape(50))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.playlist_button),
                        contentDescription = "Add to playlist",
                        tint = Color.Unspecified
                    )
                }

                IconButton(
                    onClick = {
                        viewModel.onFavoriteClicked(track)
                    },
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color(0xFFF1F4FA), RoundedCornerShape(50))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.fav_button),
                        contentDescription = "Like",
                        tint = if (isFavorite) Color(0xFFE26D6D) else Color.Unspecified
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Длительность",
                    fontSize = 14.sp,
                    color = Color(0xFFAEAFB4),
                    fontFamily = YsDisplay
                )
                Text(
                    text = formattedTime,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontFamily = YsDisplay
                )
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                ) {
                    Text(
                        text = "Добавить в плейлист",
                        fontSize = 19.sp,
                        fontFamily = YsDisplay,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                    LazyColumn {
                        items(playlists.size) { index ->
                            val playlist = playlists[index]
                            // Здесь клик обрабатывается внутри PlaylistListItem, который уже защищен
                            PlaylistListItem(playlist = playlist) {
                                scope.launch {
                                    viewModel.addTrackToPlaylist(track, playlist)
                                    Toast.makeText(context, "Добавлено в плейлист ${playlist.name}", Toast.LENGTH_SHORT).show()
                                    sheetState.hide()
                                    showBottomSheet = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}