package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Player(modifier: Modifier, context: PlatformContext, controller: MediaController)