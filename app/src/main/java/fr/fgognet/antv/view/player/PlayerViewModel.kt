package fr.fgognet.antv.view.player

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.repository.VideoDao
import fr.fgognet.antv.service.notification.NotificationService
import fr.fgognet.antv.service.player.PlayerListener
import fr.fgognet.antv.service.player.PlayerService


private const val TAG = "ANTV/PlayerViewModel"

data class PlayerData(
    val player: Player?,
    val url: String,
    val imageCode: String,
    val title: String,
    val description: String
)

@UnstableApi
@SuppressLint("StaticFieldLeak")
class PlayerViewModel : ViewModel(),
    DefaultLifecycleObserver, PlayerListener {


    fun start(context: Context) = apply { initialize(context) }

    private lateinit var _context: Context
    private val _playerdata: MutableLiveData<PlayerData> =
        MutableLiveData(
            PlayerData(
                url = "",
                imageCode = "",
                title = "",
                description = "",
                player = null
            )
        )
    val playerData: LiveData<PlayerData> get() = _playerdata
    private lateinit var observer: Observer<Player>
    private var listenerKey: Int = 0


    private fun initialize(context: Context) {
        Log.v(TAG, "init")
        this._context = context
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        this.listenerKey = PlayerService.registerListener(this)
        Log.d(TAG, "registered $listenerKey")
    }


    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        observer = Observer<Player> {
            val updatedData = PlayerData(
                url = playerData.value.url,
                imageCode = playerData.value.imageCode,
                title = playerData.value.title,
                description = playerData.value.description,
                player = it
            )
            this._playerdata.value = updatedData
        }
        PlayerService.player.observeForever(observer)
    }


    override fun onCleared() {
        Log.v(TAG, "onCleared")
        super.onCleared()
        PlayerService.player.removeObserver(observer)
        Log.d(TAG, "de-registered $listenerKey")
        PlayerService.unregisterListener(listenerKey)
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }


    override fun onIsPlayingChanged(isPlaying: Boolean) {
        NotificationService.showMediaplayerNotification(
            this._context,
            isPlaying
        )
    }

    fun retrievePlayerEntity(title: String) {
        val entity = VideoDao.get(title)
        if (entity != null) {
            val updatedData = PlayerData(
                url = entity.url,
                imageCode = entity.imageCode,
                title = entity.title,
                description = entity.description,
                player = playerData.value.player
            )
            this._playerdata.value = updatedData
        }
    }


}
