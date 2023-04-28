package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable

@Composable
expect fun Player(shouldShowControls: Boolean): Boolean

expect class PlayerController;