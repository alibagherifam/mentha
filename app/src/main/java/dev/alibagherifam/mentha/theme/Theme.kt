package dev.alibagherifam.mentha.theme

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
import androidx.compose.ui.graphics.Color.Companion as DefaultColor

private val LightColorScheme = lightColorScheme(
    primary = BrandColor.Green6,
    onPrimary = DefaultColor.White,
    primaryContainer = BrandColor.Green3,
    onPrimaryContainer = BrandColor.color15,
    secondary = BrandColor.Green5,
    onSecondary = DefaultColor.White,
    secondaryContainer = BrandColor.Green4,
    onSecondaryContainer = BrandColor.color16,
    tertiary = BrandColor.color22,
    onTertiary = DefaultColor.White,
    tertiaryContainer = BrandColor.color18,
    onTertiaryContainer = BrandColor.SkyBlue,
    error = BrandColor.LightRed,
    errorContainer = BrandColor.Pink,
    onError = DefaultColor.White,
    onErrorContainer = BrandColor.color24,
    background = BrandColor.White4,
    onBackground = BrandColor.Black200,
    surface = BrandColor.White4,
    onSurface = BrandColor.Black200,
    surfaceVariant = BrandColor.White2,
    onSurfaceVariant = DefaultColor.DarkGray,
    outline = BrandColor.Gray2,
    inverseOnSurface = BrandColor.White2,
    inverseSurface = BrandColor.color21,
    inversePrimary = BrandColor.Green,
    surfaceTint = BrandColor.Green6,
    outlineVariant = BrandColor.Silver,
    scrim = DefaultColor.Black,
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandColor.Green,
    onPrimary = BrandColor.color17,
    primaryContainer = BrandColor.Green7,
    onPrimaryContainer = BrandColor.Green3,
    secondary = BrandColor.Green2,
    onSecondary = BrandColor.color19,
    secondaryContainer = BrandColor.Green8,
    onSecondaryContainer = BrandColor.Green4,
    tertiary = BrandColor.LightBlue,
    onTertiary = BrandColor.color14,
    tertiaryContainer = BrandColor.SkyBlue,
    onTertiaryContainer = BrandColor.color13,
    error = BrandColor.White2,
    errorContainer = BrandColor.Red,
    onError = BrandColor.DarkRed,
    onErrorContainer = BrandColor.Pink,
    background = BrandColor.Black200,
    onBackground = BrandColor.White3,
    surface = BrandColor.Black200,
    onSurface = BrandColor.White3,
    surfaceVariant = DefaultColor.DarkGray,
    onSurfaceVariant = BrandColor.Silver,
    outline = BrandColor.Gray3,
    inverseOnSurface = BrandColor.Black200,
    inverseSurface = BrandColor.White3,
    inversePrimary = BrandColor.Green6,
    surfaceTint = BrandColor.Green,
    outlineVariant = DefaultColor.DarkGray,
    scrim = DefaultColor.Red,
)

@Composable
fun AppTheme(
    isInDarkMode: Boolean = isSystemInDarkTheme(),
    allowDynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        allowDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isInDarkMode) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }
        isInDarkMode -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = isInDarkMode
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = AppShapes,
        typography = AppTypography,
        content = content
    )
}
