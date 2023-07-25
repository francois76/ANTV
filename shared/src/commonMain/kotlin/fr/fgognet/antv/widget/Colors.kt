package fr.fgognet.antv.widget

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
expect fun buildColors(context: PlatformContext): ColorScheme
