package fr.fgognet.antv.view.live

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import fr.fgognet.antv.external.Images.ImageRepository
import fr.fgognet.antv.external.editorial.Editorial
import fr.fgognet.antv.external.editorial.EditorialRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "ANTV/LiveViewModel"

data class LiveViewData(
    var cards: List<CardData>,
    var title: String
)

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

    private val _liveData = MutableLiveData<LiveViewData>()
    val liveData: LiveData<LiveViewData> get() = _liveData

    override fun onStart(owner: LifecycleOwner) {
        Log.v(TAG, "onStart")
        super.onStart(owner)
        loadCardData()
    }

    fun loadCardData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var editorial: Editorial
                try {
                    editorial = EditorialRepository.getEditorialInfos()
                } catch (e: Exception) {
                    editorial = Editorial("Impossible de charger les données", "", null)
                }

                withContext(Dispatchers.Main) {
                    Log.i(TAG, "dispatching regenerated view")

                    _liveData.value = LiveViewData(generateCardData(editorial), editorial.titre)
                }
            }
        }
    }

    private fun generateCardData(editorial: Editorial): List<CardData> {
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
                diffusion.getLiveButtonLabel(),
                diffusion.isLive()
            )
            result.add(cardData)
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                for (cardData in result) {
                    val bitmap = ImageRepository.getLiveImage(cardData.imageCode)
                    Log.w(TAG, "fetched bitmap :" + cardData.imageCode)
                    withContext(Dispatchers.Main) {
                        ImageRepository.imageCodeToBitmap[cardData.imageCode] = bitmap
                    }
                }
                withContext(Dispatchers.Main) {
                    _liveData.value = _liveData.value
                }
            }
        }
        return result
    }

}