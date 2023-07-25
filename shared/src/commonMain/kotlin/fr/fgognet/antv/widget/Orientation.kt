package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable

@Composable
expect fun OrientationWrapper(portrait: @Composable () -> Unit, landscape: @Composable () -> Unit)