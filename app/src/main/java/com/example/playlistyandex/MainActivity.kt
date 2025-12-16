package com.example.playlistyandex

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.playlistyandex.ui.theme.PlaylistYandexTheme
import com.example.playlistyandex.ui.theme.YsDisplay

enum class PlaylistScreen {
    Main,
    Search,
    Settings,
    Library,
    Favorites
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistYandexTheme {
                val navController = rememberNavController()
                PlaylistHost(navController = navController)
            }
        }
    }
}

@Composable
fun PlaylistHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = PlaylistScreen.Main.name
    ) {
        composable(PlaylistScreen.Main.name) {
            MainScreen(
                onSearchClick = { navController.navigate(PlaylistScreen.Search.name) },
                onLibraryClick = { navController.navigate(PlaylistScreen.Library.name) },
                onFavoritesClick = { navController.navigate(PlaylistScreen.Favorites.name) },
                onSettingsClick = { navController.navigate(PlaylistScreen.Settings.name) }
            )
        }
        composable(PlaylistScreen.Search.name) {
            SearchScreen(onBackClick = { navController.popBackStack() })
        }
        composable(PlaylistScreen.Settings.name) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(PlaylistScreen.Library.name) {
        }
        composable(PlaylistScreen.Favorites.name) {
        }
    }
}

@Composable
fun MainScreen(
    onSearchClick: () -> Unit,
    onLibraryClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
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
                text = stringResource(id = R.string.menu_search),
                icon = R.drawable.search_icon,
                onClick = onSearchClick
            )

            MenuButton(
                text = stringResource(id = R.string.menu_library),
                icon = R.drawable.playlist,
                onClick = onLibraryClick
            )

            MenuButton(
                text = stringResource(id = R.string.menu_favorites),
                icon = R.drawable.favorite_icon,
                onClick = onFavoritesClick
            )

            MenuButton(
                text = stringResource(id = R.string.menu_settings),
                icon = R.drawable.settings_icon,
                onClick = onSettingsClick
            )
        }
    }
}

@Composable
fun SearchScreen(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        TopBar(
            title = stringResource(id = R.string.search_screen_title),
            onBackClick = onBackClick
        )
        SearchBar()

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun SettingsScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        TopBar(
            title = stringResource(id = R.string.settings_screen_title),
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            SettingsButton(
                text = stringResource(id = R.string.settings_share),
                icon = R.drawable.share_icon,
                onClick = { shareApp(context) }
            )

            SettingsButton(
                text = stringResource(id = R.string.settings_support),
                icon = R.drawable.support_icon,
                onClick = { contactSupport(context) }
            )

            SettingsButton(
                text = stringResource(id = R.string.settings_agreement),
                icon = R.drawable.arrow_icon,
                onClick = { openAgreement(context) }
            )
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
fun TopBar(title: String, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF3772E7)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.back_button_description),
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = YsDisplay,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun SearchBar() {
    var searchText by remember { mutableStateOf("") }
    val searchBackgroundColor = Color(0xFFE6E8EB)

    TextField(
        value = searchText,
        onValueChange = { newText -> searchText = newText },
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_placeholder),
                color = Color(0xFF9F9F9F)
            )
        },
        singleLine = true,
        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color(0xFF9F9F9F)
            )
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(onClick = { searchText = "" }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(id = R.string.search_clear_button_description),
                        tint = Color(0xFF9F9F9F)
                    )
                }
            }
        },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = searchBackgroundColor,
            unfocusedContainerColor = searchBackgroundColor,
            disabledContainerColor = searchBackgroundColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp))
    )
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
            painter = painterResource(id = R.drawable.arrow_icon),
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Composable
fun SettingsButton(
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
        Text(
            text = text,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(id = R.drawable.arrow_icon),
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

fun shareApp(context: Context) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app_link))
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_app)))
}

fun contactSupport(context: Context) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.support_email)))
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_subject))
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_message))
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(emailIntent)
}

fun openAgreement(context: Context) {
    val agreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.agreement_link))).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(agreementIntent)
}

@Preview(showBackground = true)
@Composable
fun PlaylistMakerPreview() {
    PlaylistYandexTheme {
        val navController = rememberNavController()
        PlaylistHost(navController)
    }
}