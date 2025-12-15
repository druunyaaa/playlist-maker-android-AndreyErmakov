package com.example.playlistyandex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistyandex.ui.theme.PlaylistYandexTheme
import com.example.playlistyandex.ui.theme.YsDisplay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistYandexTheme {
                PlaylistMakerScreen()
            }
        }
    }
}

@Composable
fun PlaylistMakerScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        HeaderTitle()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            MenuButton(
                text = "Поиск",
                icon = R.drawable.search_icon, // Убедись, что эта иконка есть в папке drawable
            ) { navigateTo(context, SearchActivity::class.java) }

            MenuButton(
                text = "Плейлисты",
                icon = R.drawable.playlist // Убедись, что эта иконка есть в папке drawable
            ) { navigateTo(context, null) }

            MenuButton(
                text = "Избранное",
                icon = R.drawable.favorite_icon // Убедись, что эта иконка есть в папке drawable
            ) { navigateTo(context, null) }

            MenuButton(
                text = "Настройки",
                icon = R.drawable.settings_icon // Убедись, что эта иконка есть в папке drawable
            ) { navigateTo(context, SettingsActivity::class.java) }
        }
    }
}

@Composable
fun HeaderTitle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color(0xFF3772E7)),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "Playlist maker",
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 16.dp),
            fontFamily = YsDisplay,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
fun MenuButton(
    text: String,
    icon: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = R.drawable.arrow_icon), // Убедись, что есть arrow_icon
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

fun navigateTo(context: Context, targetActivity: Class<*>?) {
    if (targetActivity != null) {
        val intent = Intent(context, targetActivity)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}

@Preview(showBackground = true)
@Composable
fun PlaylistMakerPreview() {
    PlaylistYandexTheme {
        PlaylistMakerScreen()
    }
}