package com.invize.masquerade.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Background = Color(0xFF0A0A0F)
val Surface = Color(0xFF13131F)
val SurfaceVariant = Color(0xFF1C1C2E)
val Primary = Color(0xFF7B61FF)
val PrimaryVariant = Color(0xFF9D87FF)
val OnPrimary = Color(0xFFFFFFFF)
val OnBackground = Color(0xFFEEEEFF)
val OnSurface = Color(0xFFCCCCDD)
val Subtle = Color(0xFF44445A)
val Error = Color(0xFFFF5370)

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurface,
    error = Error,
    outline = Subtle
)

object MasqueradeTheme {
    val colors get() = DarkColorScheme
}

@Composable
fun MasqueradeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}