package fr.fgognet.antv.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import fr.fgognet.antv.Editorial
import fr.fgognet.antv.service.StreamManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "ANTV/EditoViewModel"

class EditoViewModel(application: Application) : AndroidViewModel(application),
    DefaultLifecycleObserver {
    init {
        // Alternatively expose this as a dependency
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private val _editos = MutableLiveData<Editorial?>()
    val editos: LiveData<Editorial?> get() = _editos

    override fun onStart(owner: LifecycleOwner) {
        Log.d(TAG, "onStart")
        super.onStart(owner)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = StreamManager.getLiveInfos()
                withContext(Dispatchers.Main) {
                    _editos.value = result
                }

            }

        }
    }
}