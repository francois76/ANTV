package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable

actual class PlatformContext {
    actual fun getPlatform(): Platform {
        return Platform.JAVA
    }
}

@Composable
actual fun getPlatformContext(): PlatformContext = PlatformContext()
