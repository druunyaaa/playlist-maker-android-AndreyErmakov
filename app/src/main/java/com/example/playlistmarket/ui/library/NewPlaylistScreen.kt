package com.example.playlistmarket.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmarket.ui.YsDisplay

@Composable
fun NewPlaylistScreen(
    viewModel: PlaylistsViewModel,
    onBackClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val blueColor = Color(0xFF3772E7)
    val grayColor = Color(0xFFE6E8EB)

    val customTextSelectionColors = TextSelectionColors(
        handleColor = blueColor,
        backgroundColor = blueColor.copy(alpha = 0.4f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, "Back", tint = Color.Black)
            }
            Text("Новый плейлист", fontSize = 22.sp, fontFamily = YsDisplay, fontWeight = FontWeight.Medium, color = Color.Black, modifier = Modifier.padding(start = 12.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE6E8EB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, "Add", tint = Color.Gray, modifier = Modifier.size(80.dp))
            }
            Spacer(modifier = Modifier.height(32.dp))

            CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название*") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = blueColor,
                        unfocusedBorderColor = if (name.isNotEmpty()) blueColor else grayColor,
                        focusedLabelColor = blueColor,
                        unfocusedLabelColor = if (name.isNotEmpty()) blueColor else Color.Gray,
                        cursorColor = blueColor,
                        selectionColors = customTextSelectionColors,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = blueColor,
                        unfocusedBorderColor = if (description.isNotEmpty()) blueColor else grayColor,
                        focusedLabelColor = blueColor,
                        unfocusedLabelColor = if (description.isNotEmpty()) blueColor else Color.Gray,
                        cursorColor = blueColor,
                        selectionColors = customTextSelectionColors,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (name.isNotEmpty()) {
                        viewModel.createPlaylist(name, description)
                        onBackClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .padding(bottom = 24.dp),
                enabled = name.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = blueColor,
                    disabledContainerColor = Color(0xFF9F9F9F),
                    contentColor = Color.White,
                    disabledContentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Создать", fontSize = 16.sp, fontFamily = YsDisplay)
            }
        }
    }
}