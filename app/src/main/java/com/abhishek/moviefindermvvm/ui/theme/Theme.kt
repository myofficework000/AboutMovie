package com.abhishek.moviefindermvvm.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MovieColors = darkColorScheme(
    primary = Color(0xFFFFB74D), secondary = Color(0xFF80CBC4), surface = Color(0xFF17120E), background = Color(0xFF17120E),
)

@Composable
fun MovieFinderTheme(content: @Composable () -> Unit) = MaterialTheme(colorScheme = MovieColors, content = content)
