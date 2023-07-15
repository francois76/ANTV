package fr.fgognet.antv.view.player

import fr.fgognet.antv.widget.MediaController
import fr.fgognet.antv.widget.PlatformContext
import io.github.aakira.napier.Napier

private const val TAG = "ANTV/PlayerViewModel"

actual class PlayerViewModel : PlayerViewModelCommon() {
    override fun initialize(c: MediaController?) {
        Napier.v(tag = TAG, message = "initialize")
        if (c != null) {
            this._controller.value = c
        }
        if (controller.value == null) {
            return
        }
    }

    override fun loadMedia(title: String?) {
    }

    override fun loadPlayer(context: PlatformContext) {
    }

}