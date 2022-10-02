package fr.fgognet.antv.activity.main

import androidx.activity.ComponentActivity
import androidx.media3.common.util.UnstableApi
import fr.fgognet.antv.service.player.PlayerListener

private const val TAG = "ANTV/MainActivityListener"

@UnstableApi
class ActivityPlayerListener(var activity: ComponentActivity) : PlayerListener