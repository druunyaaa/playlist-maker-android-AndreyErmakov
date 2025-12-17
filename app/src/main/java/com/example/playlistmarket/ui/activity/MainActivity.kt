package com.example.playlistmarket.ui.activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.playlistmarket.ui.PlaylistYandexTheme
import com.example.playlistmarket.ui.navigation.PlaylistHost
import com.example.playlistmarket.ui.search.SearchViewModel

class MainActivity : ComponentActivity() {
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaylistYandexTheme {
                val navController = rememberNavController()

                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as Activity).window
                        window.statusBarColor = Color(0xFF3772E7).toArgb()
                        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                    }
                }

                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                    Box(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
                        PlaylistHost(
                            navController = navController,
                            searchViewModel = searchViewModel
                        )
                    }
                }
            }
        }
    }
}