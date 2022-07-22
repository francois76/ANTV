package fr.fgognet.antv.view.live

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import fr.fgognet.antv.Editorial
import fr.fgognet.antv.service.NetworkManager
import fr.fgognet.antv.service.StreamManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "ANTV/LiveViewModel"

data class CardData(
    var title: String,
    var subtitle: String,
    var description: String,
    var imageCode: String,
    var live: String,
    var buttonLabel: String,
    var isLive: Boolean
)

class LiveViewModel(application: Application) : AndroidViewModel(application),
    DefaultLifecycleObserver {
    init {
        // Alternatively expose this as a dependency
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private val _cards = MutableLiveData<List<CardData>?>()
    val cards: LiveData<List<CardData>?> get() = _cards

    override fun onStart(owner: LifecycleOwner) {
        Log.v(TAG, "onStart")
        super.onStart(owner)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val editorial = StreamManager.getEditorialInfos()
                val live = StreamManager.getLiveInfos()
                withContext(Dispatchers.Main) {
                    _cards.value = generateCardData(editorial, live)
                }
            }
        }


    }

    private fun generateCardData(editorial: Editorial, live: List<Int>): List<CardData> {
        Log.v(TAG, "generateCardData")
        val result = arrayListOf<CardData>()
        if (editorial.diffusions == null) {
            return result
        }
        for (diffusion in editorial.diffusions!!) {
            val cardData = CardData(
                diffusion.libelle ?: "discussion sans titre",
                diffusion.lieu ?: "lieu inconnu",
                diffusion.sujet?.replace("<br>", "\n") ?: "",
                if (diffusion.id_organe != null) "https://videos.assemblee-nationale.fr/live/images/" + diffusion.id_organe + ".jpg" else "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                diffusion.video_url ?: "",
                StreamManager.getLiveButtonLabel(
                    live.contains(diffusion.flux),
                    diffusion.heure ?: ""
                ),
                live.contains(diffusion.flux)
            )
            result.add(cardData)
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val bitmap = StreamManager.getLiveImage(cardData.imageCode)
                    Log.w(TAG, "fetched bitmap :" + cardData.imageCode)
                    withContext(Dispatchers.Main) {
                        NetworkManager.imageCodeToBitmap[cardData.imageCode] = bitmap
                    }
                }
            }
        }
        return result
    }

}