package fr.fgognet.antv.view.player

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.widget.MediaController

expect class PlayerViewModel : ViewModel {
    fun start(controller: MediaController): PlayerViewModel

    constructor()
}