package fr.fgognet.antv.service.player

import fr.fgognet.antv.widget.MediaController

expect class MediaSessionServiceImpl() {
    companion object {
        val isCasting: Boolean
        val controller: MediaController?
        
    }
}