package com.example.playlistmarket.ui.playlists

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
    var showDeleteDialog by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val debouncer = rememberClickDebouncer()
    val context = LocalContext.current

    val totalMillis = currentPlaylist.tracks.sumOf { it.trackTimeMillis }
    val durationMinutes = (totalMillis / 60000).toInt()
    val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(System.currentTimeMillis())

    val tracksCountString = pluralStringResource(R.plurals.tracks_count, currentPlaylist.tracks.size, currentPlaylist.tracks.size)
    val minutesString = pluralStringResource(R.plurals.minutes_count, durationMinutes, durationMinutes)
    val infoString = stringResource(R.string.playlist_info_format, minutesString, tracksCountString)

    val emptyShareError = stringResource(R.string.share_playlist_empty_error)
    val shareChooserTitle = stringResource(R.string.share_playlist_chooser_title)

    fun sharePlaylist() {
        if (currentPlaylist.tracks.isEmpty()) {
            Toast.makeText(context, emptyShareError, Toast.LENGTH_SHORT).show()
            return
        }

        val message = StringBuilder()
        message.append(currentPlaylist.name).append("\n")
        if (currentPlaylist.description.isNotEmpty()) {
            message.append(currentPlaylist.description).append("\n")
        }
        message.append(tracksCountString).append("\n")

        currentPlaylist.tracks.forEachIndexed { index, track ->
            val duration = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            message.append("${index + 1}. ${track.artistName} - ${track.trackName} ($duration)\n")
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message.toString())
        }
        context.startActivity(Intent.createChooser(intent, shareChooserTitle))
    }

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
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back_button_description), tint = Color.Black)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (currentPlaylist.coverImageUri != null) {
                            AsyncImage(
                                model = Uri.parse(currentPlaylist.coverImageUri),
                                contentDescription = currentPlaylist.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.default_playlist_icon),
                                contentDescription = null,
                                modifier = Modifier.wrapContentSize(),
                                contentScale = ContentScale.Inside
                            )
                        }
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
                        Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.menu_description), tint = Color.Black)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (currentPlaylist.tracks.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.empty_playlist_tracks), color = Color.Gray, fontFamily = YsDisplay)
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
                        if (currentPlaylist.coverImageUri != null) {
                            AsyncImage(
                                model = Uri.parse(currentPlaylist.coverImageUri),
                                contentDescription = null,
                                modifier = Modifier.size(45.dp).clip(RoundedCornerShape(4.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.default_playlist_icon),
                                contentDescription = null,
                                modifier = Modifier.size(45.dp).clip(RoundedCornerShape(4.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = currentPlaylist.name, fontSize = 16.sp, fontFamily = YsDisplay, color = Color.Black)
                            Text(text = tracksCountString, fontSize = 12.sp, fontFamily = YsDisplay, color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    PlaylistMenuItem(
                        text = stringResource(R.string.share_app),
                        icon = painterResource(id = R.drawable.share_icon)
                    ) {
                        showMenu = false
                        sharePlaylist()
                    }

                    PlaylistMenuItem(
                        text = stringResource(R.string.edit_playlist_info),
                        icon = rememberVectorPainter(Icons.Default.Edit)
                    ) {
                        showMenu = false
                    }

                    PlaylistMenuItem(
                        text = stringResource(R.string.delete_playlist),
                        icon = rememberVectorPainter(Icons.Default.Delete)
                    ) {
                        showMenu = false
                        showDeleteDialog = true
                    }
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                containerColor = Color.White,
                title = { Text(text = stringResource(R.string.delete_playlist_title), fontFamily = YsDisplay, fontWeight = FontWeight.Medium) },
                text = { Text(text = stringResource(R.string.delete_playlist_message, currentPlaylist.name), fontFamily = YsDisplay) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deletePlaylist()
                            showDeleteDialog = false
                            onBackClick()
                        }
                    ) {
                        Text(stringResource(R.string.yes), color = Color(0xFF3772E7), fontFamily = YsDisplay)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text(stringResource(R.string.no), color = Color(0xFF3772E7), fontFamily = YsDisplay)
                    }
                }
            )
        }
    }
}

@Composable
fun PlaylistMenuItem(
    text: String,
    icon: Painter,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = YsDisplay,
            color = Color.Black
        )
    }
}