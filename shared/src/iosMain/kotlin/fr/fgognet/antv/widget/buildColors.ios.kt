package fr.fgognet.antv.widget

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun buildColors(context: PlatformContext): ColorScheme {
    return when {
        isSystemInDarkTheme() -> darkColorScheme()
        else -> lightColorScheme()
    }
}