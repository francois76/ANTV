package fr.fgognet.antv.view.live

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.viewModelScope
import fr.fgognet.antv.external.Images.ImageRepository
import fr.fgognet.antv.external.editorial.Editorial
import fr.fgognet.antv.external.editorial.EditorialRepository
import fr.fgognet.antv.external.live.LiveRepository
import fr.fgognet.antv.external.nvs.NvsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.set

private const val TAG = "ANTV/LiveViewModel"


class LiveViewModel(application: Application) : AbstractCardListViewModel(application) {


    override fun loadCardData(params: Bundle?, force: Boolean) {
        if (super.cardListData.value?.title != null && !force) {
            return
        }
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

                    _cardListData.value =
                        CardListViewData(generateCardData(editorial), editorial.titre)
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
        viewModelScope.launch {
            var liveInfos: Map<Int, String> = hashMapOf()
            withContext(Dispatchers.IO) {
                liveInfos = LiveRepository.getLiveInfos()
                withContext(Dispatchers.Main) {
                    for (diffusion in editorial.diffusions!!) {
                        val cardData = CardData(
                            diffusion.libelle ?: "diffusion sans titre",
                            diffusion.lieu ?: "lieu inconnu",
                            diffusion.sujet?.replace("<br>", "\n") ?: "",
                            if (diffusion.id_organe != null) "https://videos.assemblee-nationale.fr/live/images/" + diffusion.id_organe + ".jpg" else "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                            "",
                            diffusion.getFormattedHour(),
                            false
                        )
                        if (!liveInfos.containsKey(diffusion.flux)) {
                            result.add(cardData)
                        } else {
                            withContext(Dispatchers.IO) {
                                val nvs = NvsRepository.getNvsByCode(
                                    liveInfos[diffusion.flux]!!
                                )
                                withContext(Dispatchers.Main) {
                                    if (liveInfos.containsKey(diffusion.flux) && diffusion.uid_referentiel == nvs.getMeetingID()
                                    ) {
                                        cardData.buttonLabel = "Live"
                                        cardData.isEnabled = true
                                        cardData.url =
                                            "https://videos.assemblee-nationale.fr/live/live${diffusion.flux}/playlist${diffusion.flux}.m3u8"
                                    }
                                    result.add(cardData)
                                }
                            }
                        }
                    }
                }
            }
            withContext(Dispatchers.IO) {
                for (cardData in result) {
                    val bitmap = ImageRepository.getLiveImage(cardData.imageCode)
                    withContext(Dispatchers.Main) {
                        ImageRepository.imageCodeToBitmap[cardData.imageCode] = bitmap
                    }
                }
                withContext(Dispatchers.Main) {
                    _cardListData.value = _cardListData.value
                }
            }
        }
        return result
    }

}