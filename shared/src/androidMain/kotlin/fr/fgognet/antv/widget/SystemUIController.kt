package fr.fgognet.antv.widget

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

actual class SystemUIController(var androidSystemUIController: SystemUiController) {


    @Composable
    actual fun SetPlatformConfiguration() {
        androidSystemUIController.setStatusBarColor(colorScheme.background)
        // TODO: find why the color is not the same as the navigationBar
        androidSystemUIController.setNavigationBarColor(colorScheme.surface)
        androidSystemUIController.systemBarsDarkContentEnabled = !isSystemInDarkTheme()
        androidSystemUIController.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    actual fun setFullScreen(fullScreen: Boolean) {
        androidSystemUIController.isSystemBarsVisible = fullScreen
    }


}

@Composable
actual fun getSystemUIController(): SystemUIController {
    return SystemUIController(androidSystemUIController = rememberSystemUiController())
}