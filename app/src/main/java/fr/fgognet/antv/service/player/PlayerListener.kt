package fr.fgognet.antv.service.player

import androidx.media3.cast.SessionAvailabilityListener
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi


@UnstableApi
interface PlayerListener : Player.Listener, SessionAvailabilityListener {
    override fun onCastSessionAvailable() {
    }

    override fun onCastSessionUnavailable() {
    }
}