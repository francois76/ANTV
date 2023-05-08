package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable

@Composable
expect fun Modal(title: String, content: String, confirmButton: String, closeCallBack: () -> Unit)