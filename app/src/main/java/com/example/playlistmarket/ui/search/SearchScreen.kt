package com.example.playlistmarket.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.ui.YsDisplay

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    onTrackClick: (Track) -> Unit
) {
    val screenState by viewModel.searchScreenState.collectAsState()
    var text by remember { mutableStateOf(viewModel.lastQuery) }
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    val blueColor = Color(0xFF3772E7)
    val inputBackgroundColor = Color(0xFFE6E8EB)

    val customTextSelectionColors = TextSelectionColors(
        handleColor = blueColor,
        backgroundColor = blueColor.copy(alpha = 0.4f)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(inputBackgroundColor)
        ) {
            CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                        viewModel.lastQuery = it
                        if (it.isEmpty()) {
                            viewModel.showHistory()
                        } else {
                            viewModel.searchDebounced(it)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .onFocusChanged {
                            isFocused = it.isFocused
                            if (it.isFocused && text.isEmpty()) viewModel.showHistory()
                        },
                    placeholder = {
                        Text(stringResource(R.string.search_placeholder), color = Color.Gray, fontFamily = YsDisplay, fontSize = 16.sp)
                    },
                    leadingIcon = { Icon(Icons.Default.Search, stringResource(R.string.search_icon_desc), tint = Color.Gray) },
                    trailingIcon = if (text.isNotEmpty()) {
                        {
                            Icon(
                                Icons.Default.Clear,
                                stringResource(R.string.search_clear_button_description),
                                tint = Color.Gray,
                                modifier = Modifier.clickable {
                                    text = ""
                                    viewModel.clearSearchText()
                                    focusManager.clearFocus()
                                }
                            )
                        }
                    } else null,
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = blueColor,
                        selectionColors = customTextSelectionColors
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        viewModel.search(text)
                        focusManager.clearFocus()
                    })
                )
            }

            if (screenState is SearchState.History && (isFocused || text.isEmpty())) {
                val historyState = screenState as SearchState.History
                if (historyState.queries.isNotEmpty()) {
                    Divider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = Color.Gray.copy(alpha = 0.3f)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        items(historyState.queries.size) { index ->
                            val query = historyState.queries[index]
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        text = query
                                        viewModel.search(query)
                                        focusManager.clearFocus()
                                    }
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.recent_icon),
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = query,
                                    fontSize = 16.sp,
                                    fontFamily = YsDisplay,
                                    color = Color.Black,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (screenState) {
            is SearchState.Success -> {
                val successState = screenState as SearchState.Success
                if (successState.list.isNotEmpty()) {
                    LazyColumn {
                        items(successState.list.size) { index ->
                            TrackListItem(successState.list[index]) {
                                viewModel.saveSearchQuery(text)
                                onTrackClick(successState.list[index])
                            }
                        }
                    }
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(painterResource(R.drawable.nothing_found_icon), null, Modifier.size(120.dp))
                            Text(stringResource(R.string.nothing_found), fontSize = 19.sp, fontFamily = YsDisplay, modifier = Modifier.padding(top = 16.dp))
                        }
                    }
                }
            }
            is SearchState.Searching -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = blueColor)
                }
            }
            is SearchState.Fail -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painterResource(R.drawable.no_internet_icon), null, Modifier.size(120.dp))
                        Text(stringResource(R.string.connection_error), fontSize = 19.sp, fontFamily = YsDisplay, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 16.dp))
                        Button(onClick = { viewModel.search(text) }, colors = ButtonDefaults.buttonColors(containerColor = blueColor), modifier = Modifier.padding(top = 24.dp)) {
                            Text(stringResource(R.string.refresh), color = Color.White)
                        }
                    }
                }
            }
            else -> { }
        }
    }
}