package com.example.playlistmarket.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.ui.YsDisplay
import com.example.playlistmarket.ui.components.clickableDebounced
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TrackListItem(
    track: Track,
    onClick: () -> Unit
) {
    val trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickableDebounced(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = track.artworkUrl100,
            contentDescription = null,
            placeholder = painterResource(R.drawable.music_icon),
            error = painterResource(R.drawable.music_icon),
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(2.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = track.trackName,
                fontSize = 16.sp,
                fontFamily = YsDisplay,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = track.artistName,
                    fontSize = 11.sp,
                    fontFamily = YsDisplay,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(text = " • ", fontSize = 11.sp, color = Color.Gray)
                Text(
                    text = trackTime,
                    fontSize = 11.sp,
                    fontFamily = YsDisplay,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(id = R.drawable.arrow_icon),
            contentDescription = null,
            tint = Color.Gray
        )
    }
}