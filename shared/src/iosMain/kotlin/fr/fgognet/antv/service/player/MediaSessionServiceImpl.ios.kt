package fr.fgognet.antv.service.player

import fr.fgognet.antv.widget.MediaController

actual class MediaSessionServiceImpl {
    actual companion object {
        actual val isCasting: Boolean
            get() = false
        actual val controller: MediaController?
            get() = MediaController()


    }

}