package fr.fgognet.antv.view.player

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.android.exoplayer2.Player
import fr.fgognet.antv.service.notification.NotificationService
import fr.fgognet.antv.service.player.PlayerListener
import fr.fgognet.antv.service.player.PlayerService


private const val TAG = "ANTV/PlayerViewModel"

class PlayerViewModel(application: Application) : AndroidViewModel(application),
    DefaultLifecycleObserver, PlayerListener {

    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> get() = _player
    private lateinit var observer: Observer<Player>
    private var listenerKey: Int = 0


    init {
        Log.v(TAG, "init")
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        this.listenerKey = PlayerService.registerListener(this)
        Log.d(TAG, "registered $listenerKey")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        observer = Observer<Player> {
            this._player.value = it
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
            this.getApplication(),
            isPlaying
        )
    }


}
