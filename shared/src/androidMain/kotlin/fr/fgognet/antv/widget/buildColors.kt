package fr.fgognet.antv.widget

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun buildColors(context: PlatformContext): ColorScheme {
    val isInDarkMode = isSystemInDarkTheme()
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val scheme = when {
            isInDarkMode -> dynamicDarkColorScheme(context.androidContext)
            else -> dynamicLightColorScheme(context.androidContext)
        }
        scheme
    } else {
        val scheme = when {
            isInDarkMode -> darkColorScheme()
            else -> lightColorScheme()
        }
        scheme
    }
}