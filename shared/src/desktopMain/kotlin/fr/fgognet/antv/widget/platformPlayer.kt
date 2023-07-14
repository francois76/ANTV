package fr.fgognet.antv.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun player(modifier:Modifier,context:PlatformContext,controller: MediaController) {
    Column(modifier=modifier.fillMaxWidth().fillMaxHeight()) {  }
}