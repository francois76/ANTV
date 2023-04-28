package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable

@Composable
expect fun orientationWrapper(portrait: @Composable () -> Unit, landscape: @Composable () -> Unit)