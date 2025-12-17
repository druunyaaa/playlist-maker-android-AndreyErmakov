package com.example.playlistmarket.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmarket.R
import com.example.playlistmarket.ui.YsDisplay

@Composable
fun HeaderTitle() {
    Box(
        modifier = Modifier.fillMaxWidth().height(64.dp).background(Color(0xFF3772E7)),
        contentAlignment = Alignment.CenterStart
    ) {
        Text("Playlist maker", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(start = 16.dp), fontFamily = YsDisplay, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun TopBar(title: String, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(56.dp).background(Color(0xFF3772E7)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
        Text(title, color = Color.White, fontSize = 20.sp, fontFamily = YsDisplay, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun MenuButton(text: String, icon: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(60.dp).clickable { onClick() }.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painterResource(icon), null, tint = Color.Black, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(16.dp))
        Text(text, fontSize = 18.sp, color = Color.Black, modifier = Modifier.weight(1f))
        Icon(painterResource(R.drawable.arrow_icon), null, tint = Color.Gray)
    }
}

@Composable
fun SettingsButton(text: String, icon: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(60.dp).clickable { onClick() }.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, fontSize = 18.sp, color = Color.Black, modifier = Modifier.weight(1f))
        Icon(painterResource(icon), null, tint = Color.Black, modifier = Modifier.size(24.dp))
    }
}