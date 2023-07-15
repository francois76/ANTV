package fr.fgognet.antv.view.player

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.service.player.MediaSessionServiceImpl
import fr.fgognet.antv.widget.MediaController
import fr.fgognet.antv.widget.PlatformContext

abstract class PlayerViewModelCommon : ViewModel() {

    fun start(controller: MediaController?) = apply { initialize(c = controller) }

    val _playerdata: MutableLiveData<PlayerData> =
        MutableLiveData(
            PlayerData(
                title = "",
                description = "",
                isCasting = MediaSessionServiceImpl.isCasting,
                isPlaying = false,
                duration = 1,
                currentPosition = 0,
                bufferedPercentage = 0,
                playbackState = 0,
            )
        )
    val playerData: LiveData<PlayerData> get() = _playerdata
    val _controller: MutableLiveData<MediaController?> = MutableLiveData(null)
    val controller: LiveData<MediaController?> get() = _controller


    abstract fun initialize(c: MediaController?)
    abstract fun loadMedia(title: String?)
    abstract fun loadPlayer(context: PlatformContext)
}

expect class PlayerViewModel() : PlayerViewModelCommon {

}