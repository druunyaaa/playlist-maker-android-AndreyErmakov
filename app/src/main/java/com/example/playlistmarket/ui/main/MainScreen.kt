package com.example.playlistmarket.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmarket.R
import com.example.playlistmarket.ui.components.HeaderTitle
import com.example.playlistmarket.ui.components.MenuButton

@Composable
fun MainScreen(
    onSearchClick: () -> Unit,
    onLibraryClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFFFFFF))) {
        HeaderTitle()
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            MenuButton(stringResource(R.string.menu_search), R.drawable.search_icon, onSearchClick)
            MenuButton("Плейлисты", R.drawable.playlist, onLibraryClick)
            MenuButton(stringResource(R.string.menu_favorites), R.drawable.favorite_icon, onFavoritesClick)
            MenuButton(stringResource(R.string.menu_settings), R.drawable.settings_icon, onSettingsClick)
        }
    }
}