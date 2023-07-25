package fr.fgognet.antv.widget

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

actual class PlatformContext(val androidContext: Context) {
    actual fun getPlatform(): Platform {
        return Platform.ANDROID
    }
}

@Composable
actual fun getPlatformContext(): PlatformContext = PlatformContext(LocalContext.current)

fun PlatformContext.findActivity(): Activity? {
    var context = this.androidContext
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

