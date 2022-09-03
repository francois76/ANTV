package fr.fgognet.antv.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PlayerView(url: String?, imageCode: String?) {
    Text(text = url ?: "")
    Text(text = imageCode ?: "")
}