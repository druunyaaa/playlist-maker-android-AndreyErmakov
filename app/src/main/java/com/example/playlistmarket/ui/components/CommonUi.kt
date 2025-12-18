package com.example.playlistmarket.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmarket.R
import com.example.playlistmarket.ui.YsDisplay

fun Modifier.clickableDebounced(
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    indication: androidx.compose.foundation.Indication? = null,
    debounceTime: Long = 500L,
    onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    this.clickable(
        enabled = enabled,
        interactionSource = interactionSource ?: remember { MutableInteractionSource() },
        indication = indication ?: androidx.compose.foundation.LocalIndication.current
    ) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > debounceTime) {
            lastClickTime = currentTime
            onClick()
        }
    }
}

class ClickDebouncer(private val debounceTime: Long = 500L) {
    private var lastClickTime = 0L

    fun click(action: () -> Unit) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > debounceTime) {
            lastClickTime = currentTime
            action()
        }
    }
}

@Composable
fun rememberClickDebouncer(debounceTime: Long = 500L): ClickDebouncer {
    return remember { ClickDebouncer(debounceTime) }
}

@Composable
fun HeaderTitle() {
    Box(
        modifier = Modifier.fillMaxWidth().height(64.dp).background(Color(0xFF3772E7)),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(stringResource(R.string.header_title), color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(start = 16.dp), fontFamily = YsDisplay, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun TopBar(title: String, onBackClick: () -> Unit) {
    val debouncer = rememberClickDebouncer()
    Row(
        modifier = Modifier.fillMaxWidth().height(56.dp).background(Color(0xFF3772E7)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { debouncer.click { onBackClick() } }) {
            Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back_button_description), tint = Color.White)
        }
        Text(title, color = Color.White, fontSize = 20.sp, fontFamily = YsDisplay, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun MenuButton(text: String, icon: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickableDebounced { onClick() }
            .padding(horizontal = 16.dp),
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
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickableDebounced { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, fontSize = 18.sp, color = Color.Black, modifier = Modifier.weight(1f))
        Icon(painterResource(icon), null, tint = Color.Black, modifier = Modifier.size(24.dp))
    }
}
