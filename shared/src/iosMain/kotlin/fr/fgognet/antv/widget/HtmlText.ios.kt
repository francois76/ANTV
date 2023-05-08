package fr.fgognet.antv.widget

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun HtmlText(html: String, modifier: Modifier) {
    Text(html)
}