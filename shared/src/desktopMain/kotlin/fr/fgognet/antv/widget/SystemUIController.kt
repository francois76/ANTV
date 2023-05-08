package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable

actual class SystemUIController {
    @Composable
    actual fun SetPlatformConfiguration() {
    }

    actual fun setFullScreen(fullScreen: Boolean) {
    }

}

@Composable
actual fun getSystemUIController() = SystemUIController()