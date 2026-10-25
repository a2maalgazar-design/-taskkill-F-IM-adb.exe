package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Amber500,
    onPrimary = Stone950,
    primaryContainer = Amber600,
    onPrimaryContainer = Amber400,
    secondary = Emerald500,
    onSecondary = Stone950,
    secondaryContainer = Emerald600,
    onSecondaryContainer = Emerald400,
    tertiary = Cyan500,
    onTertiary = Stone950,
    background = Stone950,
    onBackground = Stone100,
    surface = Stone900,
    onSurface = Stone100,
    surfaceVariant = Stone850,
    onSurfaceVariant = Stone300,
    outline = Stone700,
    outlineVariant = Stone800,
    error = Rose500,
    onError = Stone950
)

private val LightColorScheme = darkColorScheme( // Defaulting to the same polished dark tone
    primary = Amber500,
    onPrimary = Stone950,
    background = Stone950,
    onBackground = Stone100,
    surface = Stone900,
    onSurface = Stone100
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false, // Keep consistent branding across Android devices
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
