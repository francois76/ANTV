package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable

@Composable
actual fun orientationWrapper(
    portrait: @Composable () -> Unit,
    landscape: @Composable () -> Unit
) {
    landscape()
}