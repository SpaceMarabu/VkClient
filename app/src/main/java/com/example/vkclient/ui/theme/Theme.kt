package com.example.vkclient.ui.theme

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@SuppressLint("ConflictingOnColor")
private val DarkColorScheme = darkColorScheme(
    primary = Black900,
    secondary = Black900,
    onPrimary = Color.White,
    onSecondary = Black500
)

@SuppressLint("ConflictingOnColor")
private val LightColorScheme = lightColorScheme(
    primary = Color.Gray,
    secondary = Color.Gray.copy(alpha = 0.05f),
    onPrimary = Black900,
    onSecondary = Black500,
    onSurface = Color.White
)

@Composable
fun VkClientTheme(
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}