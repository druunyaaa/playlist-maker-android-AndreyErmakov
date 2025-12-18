package com.example.playlistmarket.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmarket.R
import com.example.playlistmarket.ui.components.SettingsButton
import com.example.playlistmarket.ui.components.TopBar
import com.example.playlistmarket.utils.contactSupport
import com.example.playlistmarket.utils.openAgreement
import com.example.playlistmarket.utils.shareApp

@Composable
fun SettingsScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFFFFFF))) {
        TopBar(stringResource(R.string.settings_screen_title), onBackClick)
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            SettingsButton(stringResource(R.string.settings_share), R.drawable.share_icon) { shareApp(context) }
            SettingsButton(stringResource(R.string.settings_support), R.drawable.support_icon) { contactSupport(context) }
            SettingsButton(stringResource(R.string.settings_agreement), R.drawable.arrow_icon) { openAgreement(context) }
        }
    }
}