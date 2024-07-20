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
    primary = Color.White,
    secondary = Color.White,
    onPrimary = Black900,
    onSecondary = Black500
)

@Composable
fun VkClientTheme(
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}