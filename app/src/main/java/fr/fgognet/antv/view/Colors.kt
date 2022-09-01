package fr.fgognet.antv.view

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun buildColors(context: Context): ColorScheme {
    val isInDarkMode = isSystemInDarkTheme()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val scheme = when {
            isInDarkMode -> dynamicDarkColorScheme(context)
            else -> dynamicLightColorScheme(context)
        }
        return scheme
    }
    return MaterialTheme.colorScheme
}