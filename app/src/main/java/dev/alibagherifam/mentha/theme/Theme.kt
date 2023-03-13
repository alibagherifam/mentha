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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = CustomColor.Green6,
    onPrimary = Color.White,
    primaryContainer = CustomColor.Green3,
    onPrimaryContainer = CustomColor.color15,
    secondary = CustomColor.Green5,
    onSecondary = Color.White,
    secondaryContainer = CustomColor.Green4,
    onSecondaryContainer = CustomColor.color16,
    tertiary = CustomColor.color22,
    onTertiary = Color.White,
    tertiaryContainer = CustomColor.color18,
    onTertiaryContainer = CustomColor.SkyBlue,
    error = CustomColor.LightRed,
    errorContainer = CustomColor.Pink,
    onError = Color.White,
    onErrorContainer = CustomColor.color24,
    background = CustomColor.White4,
    onBackground = CustomColor.Black200,
    surface = CustomColor.White4,
    onSurface = CustomColor.Black200,
    surfaceVariant = CustomColor.White2,
    onSurfaceVariant = Color.DarkGray,
    outline = CustomColor.Gray2,
    inverseOnSurface = CustomColor.White2,
    inverseSurface = CustomColor.color21,
    inversePrimary = CustomColor.Green,
    surfaceTint = CustomColor.Green6,
    outlineVariant = CustomColor.Silver,
    scrim = Color.Black,
)

private val DarkColorScheme = darkColorScheme(
    primary = CustomColor.Green,
    onPrimary = CustomColor.color17,
    primaryContainer = CustomColor.Green7,
    onPrimaryContainer = CustomColor.Green3,
    secondary = CustomColor.Green2,
    onSecondary = CustomColor.color19,
    secondaryContainer = CustomColor.Green8,
    onSecondaryContainer = CustomColor.Green4,
    tertiary = CustomColor.LightBlue,
    onTertiary = CustomColor.color14,
    tertiaryContainer = CustomColor.SkyBlue,
    onTertiaryContainer = CustomColor.color13,
    error = CustomColor.White2,
    errorContainer = CustomColor.Red,
    onError = CustomColor.DarkRed,
    onErrorContainer = CustomColor.Pink,
    background = CustomColor.Black200,
    onBackground = CustomColor.White3,
    surface = CustomColor.Black200,
    onSurface = CustomColor.White3,
    surfaceVariant = Color.DarkGray,
    onSurfaceVariant = CustomColor.Silver,
    outline = CustomColor.Gray3,
    inverseOnSurface = CustomColor.Black200,
    inverseSurface = CustomColor.White3,
    inversePrimary = CustomColor.Green6,
    surfaceTint = CustomColor.Green,
    outlineVariant = Color.DarkGray,
    scrim = Color.Red,
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
