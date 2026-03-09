package com.example.zadacha2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF009688),
    secondary = Color(0xFFFF005E),
    background = Color(0xFF8BC34A),
    surface = Color.White,
)

@Composable
fun FavoriteTwitterSearchesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}