package com.disha.career.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Indigo = Color(0xFF3F3D9E)
private val Saffron = Color(0xFFF2994A)

private val LightColors = lightColorScheme(
    primary = Indigo,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE2E0FF),
    onPrimaryContainer = Color(0xFF14105E),
    secondary = Saffron,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE6D1),
    onSecondaryContainer = Color(0xFF4A2300),
    background = Color(0xFFFBFAFF),
    surface = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFC1BFFF),
    onPrimary = Color(0xFF221F74),
    primaryContainer = Color(0xFF39368A),
    onPrimaryContainer = Color(0xFFE2E0FF),
    secondary = Color(0xFFFFB77C),
    onSecondary = Color(0xFF4A2300),
    secondaryContainer = Color(0xFF6A3A10),
    onSecondaryContainer = Color(0xFFFFE6D1),
)

@Composable
fun DishaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content,
    )
}
