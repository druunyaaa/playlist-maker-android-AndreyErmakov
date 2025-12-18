package com.example.playlistmarket.ui.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.ui.YsDisplay
import com.example.playlistmarket.ui.search.TrackListItem

@Composable
fun FavoritesScreen(
    onTrackClick: (Track) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val application = context.applicationContext as android.app.Application
    val viewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModel.getViewModelFactory(application)
    )

    val favoriteTracks by viewModel.favoriteTracks.collectAsState(initial = emptyList())

    var trackToDelete by remember { mutableStateOf<Track?>(null) }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        if (favoriteTracks.isEmpty()) {
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
                    text = stringResource(R.string.empty_library),
                    fontSize = 19.sp,
                    fontFamily = YsDisplay,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 88.dp, top = 16.dp)
            ) {
                items(favoriteTracks) { track ->
                    TrackListItem(
                        track = track,
                        onClick = { onTrackClick(track) },
                        onLongClick = {
                            trackToDelete = track
                        }
                    )
                }
            }
        }

        if (trackToDelete != null) {
            AlertDialog(
                onDismissRequest = { trackToDelete = null },
                containerColor = Color.White,
                title = {
                    Text(
                        text = stringResource(id = R.string.delete_track_title),
                        fontFamily = YsDisplay,
                        fontWeight = FontWeight.Medium
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.delete_track_message),
                        fontFamily = YsDisplay
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            trackToDelete?.let { viewModel.deleteTrack(it) }
                            trackToDelete = null
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.yes),
                            color = Color(0xFF3772E7),
                            fontFamily = YsDisplay
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { trackToDelete = null }
                    ) {
                        Text(
                            text = stringResource(id = R.string.no),
                            color = Color(0xFF3772E7),
                            fontFamily = YsDisplay
                        )
                    }
                }
            )
        }
    }
}