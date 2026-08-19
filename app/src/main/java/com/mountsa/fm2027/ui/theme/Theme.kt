package com.mountsa.fm2027.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF2ECC71),      // Vibrant Green
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF1B5E20),
    onPrimaryContainer = Color(0xFF69F0AE),
    secondary = Color(0xFF0A141D),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1E1E1E),
    onSecondaryContainer = Color(0xFF2ECC71),
    tertiary = Color(0xFF3498DB),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF0D47A1),
    onTertiaryContainer = Color(0xFF64B5F6),
    background = Color(0xFF121212),   // Dark background
    onBackground = Color.White,       // White text on dark background
    surface = Color(0xFF1E1E1E),      // Dark surface
    onSurface = Color.White,          // White text on surface
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFB0B0B0),
    outline = Color(0xFF424242),
    error = Color(0xFFCF6679),
    onError = Color.Black
)

@Composable
fun Fm2027Theme(
    darkTheme: Boolean = false, // Parameter ignored - always dark theme
    content: @Composable () -> Unit
) {
    // Always use dark color scheme for black background
    val colorScheme = DarkColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set status bar and navigation bar to black
            window.statusBarColor = Color.Black.toArgb()
            window.navigationBarColor = Color.Black.toArgb()

            val controller = WindowCompat.getInsetsController(window, view)
            // Use light icons (white) on dark background
            controller.isAppearanceLightStatusBars = false
            controller.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}