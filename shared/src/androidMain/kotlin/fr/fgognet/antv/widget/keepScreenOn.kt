package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable

@Composable
actual fun keepScreenOn(context: PlatformContext) {
    context.findActivity()?.window?.decorView?.keepScreenOn = true
}