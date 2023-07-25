package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable


expect class SystemUIController {
    @Composable
    fun SetPlatformConfiguration()

    fun setFullScreen(fullScreen: Boolean)

}

@Composable
expect fun getSystemUIController(): SystemUIController