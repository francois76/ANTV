package fr.fgognet.antv.service.player

import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener

interface PlayerListener : Player.Listener, SessionAvailabilityListener