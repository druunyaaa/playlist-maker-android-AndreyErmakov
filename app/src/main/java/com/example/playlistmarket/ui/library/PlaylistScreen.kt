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
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.ui.YsDisplay
import com.example.playlistmarket.ui.components.rememberClickDebouncer
import com.example.playlistmarket.ui.search.TrackListItem
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    viewModel: PlaylistViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val playlist by viewModel.playlist.collectAsState(initial = null)

    if (playlist == null) return

    val currentPlaylist = playlist!!
    var showMenu by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val debouncer = rememberClickDebouncer()

    val totalMillis = currentPlaylist.tracks.sumOf { it.trackTimeMillis }
    val durationMinutes = (totalMillis / 60000).toInt()
    val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(System.currentTimeMillis())

    // Теперь функция getPluralString доступна, ошибки пропадут
    val tracksCountString = getPluralString(currentPlaylist.tracks.size, "трек", "трека", "треков")
    val minutesString = getPluralString(durationMinutes, "минута", "минуты", "минут")
    val infoString = "$minutesString • $tracksCountString"

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                    IconButton(
                        onClick = { debouncer.click { onBackClick() } },
                        modifier = Modifier.padding(bottom = 24.dp).offset(x = (-12).dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.default_playlist_icon),
                            contentDescription = null,
                            modifier = Modifier.wrapContentSize(),
                            contentScale = ContentScale.Inside
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = currentPlaylist.name,
                        fontSize = 24.sp,
                        fontFamily = YsDisplay,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = currentPlaylist.description.ifEmpty { year },
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
                        modifier = Modifier.padding(top = 8.dp).offset(x = (-12).dp)
                    ) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (currentPlaylist.tracks.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text("В этом плейлисте нет треков", color = Color.Gray, fontFamily = YsDisplay)
                    }
                }
            } else {
                items(currentPlaylist.tracks.size) { index ->
                    Box(modifier = Modifier.padding(bottom = 8.dp)) {
                        TrackListItem(
                            track = currentPlaylist.tracks[index],
                            onClick = { onTrackClick(currentPlaylist.tracks[index]) }
                        )
                    }
                }
            }
        }

        if (showMenu) {
            ModalBottomSheet(
                onDismissRequest = { showMenu = false },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                Column(modifier = Modifier.padding(bottom = 32.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.default_playlist_icon),
                            contentDescription = null,
                            modifier = Modifier.size(45.dp).clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = currentPlaylist.name, fontSize = 16.sp, fontFamily = YsDisplay, color = Color.Black)
                            Text(text = tracksCountString, fontSize = 12.sp, fontFamily = YsDisplay, color = Color.Gray)
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

@Composable
fun MenuActionItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontFamily = YsDisplay,
        color = Color.Black,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 16.dp, horizontal = 16.dp)
    )
}

fun getPluralString(count: Int, one: String, two: String, five: String): String {
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