package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable

@Composable
expect fun player(context:PlatformContext, controller: MediaController, onclick: ()->Unit)