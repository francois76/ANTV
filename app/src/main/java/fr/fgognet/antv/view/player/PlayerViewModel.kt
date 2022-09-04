package fr.fgognet.antv.view.player

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.exoplayer2.Player
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.service.notification.NotificationService
import fr.fgognet.antv.service.player.PlayerListener
import fr.fgognet.antv.service.player.PlayerService


private const val TAG = "ANTV/PlayerViewModel"

@SuppressLint("StaticFieldLeak")
class PlayerViewModel : ViewModel(),
    DefaultLifecycleObserver, PlayerListener {


    fun start(context: Context) = apply { initialize(context) }

    private lateinit var _context: Context
    private val _player: MutableLiveData<Player?> = MutableLiveData(null)
    val player: LiveData<Player?> get() = _player
    private lateinit var observer: Observer<Player>
    private var listenerKey: Int = 0


    fun initialize(context: Context) {
        Log.v(TAG, "init")
        this._context = context
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
            this._context,
            isPlaying
        )
    }


}
