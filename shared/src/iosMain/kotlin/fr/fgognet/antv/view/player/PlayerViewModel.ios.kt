package fr.fgognet.antv.view.player

import dev.icerock.moko.mvvm.livedata.setValue
import fr.fgognet.antv.repository.VideoDao
import fr.fgognet.antv.service.player.AVPlayerListener
import fr.fgognet.antv.service.player.MediaSessionServiceImpl
import fr.fgognet.antv.widget.MediaController
import fr.fgognet.antv.widget.PlatformContext
import io.github.aakira.napier.Napier
import kotlinx.cinterop.CValue
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import platform.AVFoundation.*
import platform.CoreMedia.CMTime
import platform.CoreMedia.CMTimeGetSeconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private const val TAG = "ANTV/PlayerViewModel"

actual class PlayerViewModel : PlayerViewModelCommon(), AVPlayerListener {
    override fun initialize(c: MediaController?) {
        Napier.v(tag = TAG, message = "initialize")
        if (c != null) {
            this._controller.setValue(c, true)
        }
        if (controller.value == null) {
            return
        }
        MediaSessionServiceImpl.addListener(this)

        val t = this
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                ticker().collect { isPlaying ->
                    if (isPlaying) {
                        t._playerdata.value = t.playerData.value.copy(
                            currentPosition = iosTimeToSecond(player()?.currentItem?.currentTime())
                                ?: 0,
                            duration = iosTimeToSecond(player()?.currentItem?.duration)
                                ?: 1,
                            bufferedPercentage = 50 // TODO
                                ?: 0,
                            isEnded = false, // TODO
                        )
                    }
                }
            }
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Napier.v(tag = TAG, message = "onIsPlayingChanged")
        this._playerdata.value = this.playerData.value.copy(
            isPlaying = isPlaying,
        )
    }

    override fun loadMedia(title: String?) {
        Napier.v(tag = TAG, message = "loadMedia")
        if (title == null) {
            loadCurrentMedia()
        } else {
            updateCurrentMedia(title)
        }
    }

    private fun loadCurrentMedia() {
        Napier.v(tag = TAG, message = "loadCurrentMedia")
        this._playerdata.value = this.playerData.value.copy(
            title = "TODO",
            description = "TODO",
            currentPosition = iosTimeToSecond(player()?.currentItem?.currentTime())
                ?: 0,
            duration = iosTimeToSecond(player()?.currentItem?.duration)
                ?: 1,
            bufferedPercentage = 50 // TODO
                ?: 0,
            isEnded = false, // TODO
            isPlaying = player()?.timeControlStatus == AVPlayerTimeControlStatusPlaying,
        )
    }

    private fun updateCurrentMedia(title: String) {
        Napier.v(tag = TAG, message = "updateCurrentMedia")
        if (title == playerData.value.title) {
            return
        }
        val entity = VideoDao.get(title)
        if (entity == null) {
            if (MediaSessionServiceImpl.controller != null) {
                this._playerdata.value = this.playerData.value.copy(
                    title = "TODO",
                    description = "TODO",
                    duration = 200,
                    currentPosition = 0,
                    isPlaying = true,
                    bufferedPercentage = 50,
                    isEnded = false,
                )
            }
        } else {
            MediaSessionServiceImpl.controller?.setMediaItem(entity)
            MediaSessionServiceImpl.controller?.play()

            this._playerdata.value = this.playerData.value.copy(
                title = entity.title,
                description = entity.description,
                currentPosition = iosTimeToSecond(player()?.currentItem?.currentTime())
                    ?: 0,
                duration = iosTimeToSecond(player()?.currentItem?.duration)
                    ?: 1,
                bufferedPercentage = 50 // TODO
                    ?: 0,
                isEnded = false, // TODO
            )
        }

    }

    override fun loadPlayer(context: PlatformContext) {
        Napier.v(tag = TAG, message = "loadPlayer")
        MediaSessionServiceImpl.loadController()
        this._controller.setValue(MediaSessionServiceImpl.controller, true)
    }

    private fun player(): AVPlayer? {
        return MediaSessionServiceImpl.controller?.iosMediaController?.player
    }

    private fun iosTimeToSecond(value: CValue<CMTime>?): Long? {
        if (value == null) {
            return null;
        }
        val cmTime = CMTimeGetSeconds(value);
        return if (cmTime.isNaN()) null else cmTime.toDuration(DurationUnit.SECONDS).inWholeSeconds
    }

    private fun ticker(): Flow<Boolean> = flow {
        while (true) {
            delay(1000)
            emit(player()?.rate != "0".toFloat() && player()?.error == null)
        }
    }

}