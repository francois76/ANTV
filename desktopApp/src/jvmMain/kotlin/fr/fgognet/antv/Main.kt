package fr.fgognet.antv

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import fr.fgognet.antv.view.main.ANTVApp


fun main() = application {
    application {
        Window(
            onCloseRequest = ::exitApplication, title = "ANTV",
            state = WindowState(
                position = WindowPosition.Aligned(Alignment.Center),
            ),
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                ANTVApp()
            }

        }
    }
}

