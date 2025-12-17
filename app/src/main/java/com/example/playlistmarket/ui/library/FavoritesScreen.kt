package com.example.playlistmarket.ui.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.ui.YsDisplay
import com.example.playlistmarket.ui.search.TrackListItem
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun FavoritesScreen(
    playlistsViewModel: PlaylistsViewModel,
    onTrackClick: (Track) -> Unit
) {
    val favoriteTracks by playlistsViewModel.favoriteTracks.collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (favoriteTracks.isEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nothing_found_icon),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
                Text(
                    text = "Ваша медиатека пуста",
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
                contentPadding = PaddingValues(bottom = 16.dp) // Небольшой отступ снизу
            ) {
                items(favoriteTracks.size) { index ->
                    TrackListItem(track = favoriteTracks[index]) {
                        onTrackClick(favoriteTracks[index])
                    }
                }
            }
        }
    }
}