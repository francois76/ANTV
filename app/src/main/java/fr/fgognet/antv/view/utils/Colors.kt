package fr.fgognet.antv.view.utils

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun buildColors(context: Context): ColorScheme {
    val isInDarkMode = isSystemInDarkTheme()
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val scheme = when {
            isInDarkMode -> dynamicDarkColorScheme(context)
            else -> dynamicLightColorScheme(context)
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