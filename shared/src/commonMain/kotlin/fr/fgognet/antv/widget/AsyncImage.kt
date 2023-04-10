package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter

@Composable
expect fun AsyncImage(
    modifier: Modifier,
    placeholder: Painter,
    model: String,
    contentDescription: String
)