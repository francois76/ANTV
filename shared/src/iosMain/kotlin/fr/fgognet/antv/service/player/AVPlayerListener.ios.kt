package fr.fgognet.antv.service.player

interface AVPlayerListener {

    fun onIsPlayingChanged(isPlaying: Boolean) {}
}