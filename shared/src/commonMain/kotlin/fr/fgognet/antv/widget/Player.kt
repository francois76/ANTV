package fr.fgognet.antv.widget

import androidx.compose.runtime.Composable
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.view.player.PlayerData


@Composable
expect fun Player(shouldShowControls: Boolean, controller: MediaController): Boolean

expect class MediaController

expect class MediaSessionServiceImpl() {
    companion object {
        val isCasting: Boolean
        val controller: MediaController
    }
}

abstract class PlayerViewModelCommon : ViewModel() {

    fun start(controller: MediaController?) = apply { initialize(c = controller) }

    private val _playerdata: MutableLiveData<PlayerData> =
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


}
