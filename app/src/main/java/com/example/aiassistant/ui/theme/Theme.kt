package com.example.aiassistant.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Futuristic Dark Theme for TaskIt
 * Features cyberpunk-inspired colors and modern aesthetics
 */
private val FuturisticDarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = DarkBackground,
    primaryContainer = ElectricBlueVariant,
    onPrimaryContainer = TextPrimary,
    
    secondary = NeonGreen,
    onSecondary = DarkBackground,
    secondaryContainer = NeonPurple,
    onSecondaryContainer = TextPrimary,
    
    tertiary = NeonPink,
    onTertiary = DarkBackground,
    tertiaryContainer = AccentPurple,
    onTertiaryContainer = TextPrimary,
    
    background = DarkBackground,
    onBackground = TextPrimary,
    
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = MediumSurface,
    onSurfaceVariant = TextSecondary,
    
    outline = TextTertiary,
    outlineVariant = TextTertiary,
    
    error = AccentRed,
    onError = TextPrimary
)

/**
 * Light theme variant (will be primarily dark-focused app)
 */
private val FuturisticLightColorScheme = lightColorScheme(
    primary = DeepBlue,
    onPrimary = TextPrimary,
    primaryContainer = ElectricBlue,
    onPrimaryContainer = DarkBackground,
    
    secondary = AccentGreen,
    onSecondary = TextPrimary,
    secondaryContainer = NeonGreen,
    onSecondaryContainer = DarkBackground,
    
    tertiary = AccentPurple,
    onTertiary = TextPrimary,
    tertiaryContainer = NeonPink,
    onTertiaryContainer = DarkBackground
)

/**
 * Main theme composable for the TaskIt app
 * Always uses dark theme for futuristic feel
 */
@Composable
fun AiassistantTheme(
    darkTheme: Boolean = true, // Force dark theme for futuristic look
    dynamicColor: Boolean = false, // Disable dynamic colors to maintain brand consistency
    content: @Composable () -> Unit
) {
    // Always use our custom futuristic dark theme
    val colorScheme = FuturisticDarkColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set status bar color to match app background
            window.statusBarColor = colorScheme.background.toArgb()
            // Make status bar icons light colored for dark background
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}