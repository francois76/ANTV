package fr.fgognet.antv.view.player

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.gms.cast.framework.CastContext
import fr.fgognet.antv.service.PlayerService


private const val TAG = "ANTV/PlayerViewModel"

class PlayerViewModel(application: Application) : AndroidViewModel(application),
    DefaultLifecycleObserver, SessionAvailabilityListener, Player.Listener {

    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> get() = _player
    private lateinit var castContext: CastContext


    init {
        // Alternatively expose this as a dependency
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        // Getting the cast context later than onStart can cause device discovery not to take place.
        try {
            Log.v(TAG, "init")
            castContext = CastContext.getSharedInstance(this.getApplication())
            val player =
                PlayerService.getPlayer(castContext, getApplication<Application>().baseContext)
            PlayerService.registerCastListener(this)
            this._player.value = player
        } catch (e: RuntimeException) {
            Log.e(TAG, e.toString())
        }
    }


    override fun onCleared() {
        Log.v(TAG, "onCleared")
        super.onCleared()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }


    override fun onCastSessionAvailable() {
        Log.v(TAG, "onCastSessionAvailable")
        PlayerService.cast()
        this._player.value =
            PlayerService.getPlayer(castContext, getApplication<Application>().applicationContext)
    }

    override fun onCastSessionUnavailable() {
        Log.v(TAG, "onCastSessionUnavailable")
        PlayerService.stopCast()
        this._player.value =
            PlayerService.getPlayer(castContext, getApplication<Application>().applicationContext)
    }


}
