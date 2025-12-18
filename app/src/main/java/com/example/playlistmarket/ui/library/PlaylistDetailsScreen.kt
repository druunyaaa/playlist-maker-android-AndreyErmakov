package com.example.playlistmarket.ui.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.ui.YsDisplay
import com.example.playlistmarket.ui.search.TrackListItem
import com.example.playlistmarket.ui.components.MenuActionItem
import com.example.playlistmarket.ui.components.getPluralString
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailsScreen(
    playlist: Playlist,
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val totalMillis = playlist.tracks.sumOf { it.trackTimeMillis }
    val durationMinutes = (totalMillis / 60000).toInt()
    val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(playlist.id)

    val tracksCountString = getPluralString(playlist.tracks.size, "трек", "трека", "треков")
    val minutesString = getPluralString(durationMinutes, "минута", "минуты", "минут")
    val infoString = "$minutesString • $tracksCountString"

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFE6E8EB))) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .offset(x = (-12).dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }

                    Image(
                        painter = painterResource(id = R.drawable.default_playlist_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = playlist.name,
                        fontSize = 24.sp,
                        fontFamily = YsDisplay,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = year,
                        fontSize = 18.sp,
                        fontFamily = YsDisplay,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = infoString,
                        fontSize = 18.sp,
                        fontFamily = YsDisplay,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .offset(x = (-12).dp)
                    ) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.Black)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (playlist.tracks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("В этом плейлисте нет треков", color = Color.Gray, fontFamily = YsDisplay)
                    }
                }
            } else {
                items(playlist.tracks.size) { index ->
                    Box(modifier = Modifier.padding(bottom = 8.dp)) {
                        TrackListItem(
                            track = playlist.tracks[index],
                            onClick = { onTrackClick(playlist.tracks[index]) }
                        )
                    }
                }
            }
        }

        if (showMenu) {
            ModalBottomSheet(
                onDismissRequest = { showMenu = false },
                sheetState = sheetState,
                containerColor = Color.White,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                Column(modifier = Modifier.padding(bottom = 32.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.default_playlist_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(45.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = playlist.name,
                                fontSize = 16.sp,
                                fontFamily = YsDisplay,
                                color = Color.Black
                            )
                            Text(
                                text = tracksCountString,
                                fontSize = 12.sp,
                                fontFamily = YsDisplay,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    MenuActionItem("Поделиться") { showMenu = false }
                    MenuActionItem("Редактировать информацию") { showMenu = false }
                    MenuActionItem("Удалить плейлист") { showMenu = false }
                }
            }
        }
    }
}