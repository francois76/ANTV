package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun HtmlText(html: String, modifier: Modifier = Modifier)

