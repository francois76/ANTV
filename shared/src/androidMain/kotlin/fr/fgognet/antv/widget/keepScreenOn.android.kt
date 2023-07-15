package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable

private const val TAG = "ANTV/WidgetKeepScreenOn"

@Composable
actual fun KeepScreenOn(context: PlatformContext, value: Boolean) {

    context.findActivity()?.window?.decorView?.keepScreenOn = value
}