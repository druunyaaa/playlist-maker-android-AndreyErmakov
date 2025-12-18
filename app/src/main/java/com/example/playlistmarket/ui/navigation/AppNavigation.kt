package com.example.playlistmarket.ui.navigation

import android.app.Application
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.ui.components.TopBar
import com.example.playlistmarket.ui.favorites.FavoritesScreen
import com.example.playlistmarket.ui.playlists.*
import com.example.playlistmarket.ui.main.MainScreen
import com.example.playlistmarket.ui.player.PlayerScreen
import com.example.playlistmarket.ui.player.PlayerViewModel
import com.example.playlistmarket.ui.search.SearchScreen
import com.example.playlistmarket.ui.search.SearchViewModel
import com.google.gson.Gson
import com.example.playlistmarket.ui.settings.SettingsScreen

enum class PlaylistScreen {
    Main, Search, Settings, Library, Favorites, NewPlaylist, Player, PlaylistDetails
}

@Composable
fun PlaylistHost(
    navController: NavHostController
) {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    val context = LocalContext.current.applicationContext as Application

    fun navigateSafe(route: String) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > 500) {
            lastClickTime = currentTime
            navController.navigate(route)
        }
    }

    NavHost(navController = navController, startDestination = PlaylistScreen.Main.name) {

        composable(PlaylistScreen.Main.name) {
            MainScreen(
                onSearchClick = { navigateSafe(PlaylistScreen.Search.name) },
                onLibraryClick = { navigateSafe(PlaylistScreen.Library.name) },
                onFavoritesClick = { navigateSafe(PlaylistScreen.Favorites.name) },
                onSettingsClick = { navigateSafe(PlaylistScreen.Settings.name) }
            )
        }

        composable(PlaylistScreen.Search.name) {
            val searchViewModel: SearchViewModel = viewModel(
                factory = SearchViewModel.getViewModelFactory(context)
            )

            Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
                TopBar(stringResource(R.string.search_screen_title)) { navController.popBackStack() }
                SearchScreen(
                    modifier = Modifier.weight(1f),
                    viewModel = searchViewModel,
                    onTrackClick = { track ->
                        val json = Uri.encode(Gson().toJson(track))
                        navigateSafe("${PlaylistScreen.Player.name}/$json")
                    }
                )
            }
        }

        composable(PlaylistScreen.Favorites.name) {

            Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
                TopBar(stringResource(R.string.menu_favorites)) { navController.popBackStack() }
                FavoritesScreen(
                    onTrackClick = { track ->
                        val json = Uri.encode(Gson().toJson(track))
                        navigateSafe("${PlaylistScreen.Player.name}/$json")
                    }
                )
            }
        }

        composable(PlaylistScreen.Settings.name) {
            SettingsScreen { navController.popBackStack() }
        }

        composable(PlaylistScreen.Library.name) {
            val viewModel: PlaylistsViewModel = viewModel(factory = PlaylistsViewModel.getViewModelFactory(context))
            Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
                TopBar(stringResource(R.string.menu_library)) { navController.popBackStack() }
                PlaylistsScreen(
                    playlistsViewModel = viewModel,
                    addNewPlaylist = { navigateSafe(PlaylistScreen.NewPlaylist.name) },
                    navigateToPlaylist = { playlistId ->
                        navigateSafe("${PlaylistScreen.PlaylistDetails.name}/$playlistId")
                    }
                )
            }
        }

        composable(PlaylistScreen.NewPlaylist.name) {
            val viewModel: PlaylistsViewModel = viewModel(factory = PlaylistsViewModel.getViewModelFactory(context))
            NewPlaylistScreen(viewModel) { navController.popBackStack() }
        }

        composable(
            route = "${PlaylistScreen.PlaylistDetails.name}/{playlistId}",
            arguments = listOf(navArgument("playlistId") { type = NavType.LongType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: 0L

            val viewModel: PlaylistViewModel = viewModel(
                factory = PlaylistViewModel.getViewModelFactory(playlistId, context)
            )

            PlaylistScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onTrackClick = { track ->
                    val json = Uri.encode(Gson().toJson(track))
                    navigateSafe("${PlaylistScreen.Player.name}/$json")
                }
            )
        }

        composable(
            route = "${PlaylistScreen.Player.name}/{track}",
            arguments = listOf(navArgument("track") { type = NavType.StringType })
        ) { backStackEntry ->
            val trackJson = backStackEntry.arguments?.getString("track")
            val track = Gson().fromJson(trackJson, Track::class.java)
            val playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.getViewModelFactory(context))
            PlayerScreen(track, playerViewModel) { navController.popBackStack() }
        }
    }
}