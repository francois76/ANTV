package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable

@Composable
actual fun KeepScreenOn(context: PlatformContext) {
    context.findActivity()?.window?.decorView?.keepScreenOn = true
}