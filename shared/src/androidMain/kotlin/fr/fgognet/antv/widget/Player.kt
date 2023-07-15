package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import androidx.media3.common.Player.STATE_ENDED


@Composable
actual fun getStateEnded(): Int {
    return STATE_ENDED
}