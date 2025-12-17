package com.example.playlistmarket.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.ui.YsDisplay
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TrackListItem(track: Track, onClick: () -> Unit) {
    val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.music_icon),
            contentDescription = null,
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.LightGray),
            contentScale = ContentScale.Inside
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(text = track.trackName, fontSize = 16.sp, fontFamily = YsDisplay, maxLines = 1)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = track.artistName, fontSize = 11.sp, color = Color.Gray, fontFamily = YsDisplay, maxLines = 1)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "•", fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = formattedTime, fontSize = 11.sp, color = Color.Gray, fontFamily = YsDisplay)
            }
        }
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.Transparent
        )
    }
}