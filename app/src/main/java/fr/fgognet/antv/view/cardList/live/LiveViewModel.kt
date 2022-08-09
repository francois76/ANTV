package fr.fgognet.antv.view.cardList.live

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.viewModelScope
import fr.fgognet.antv.R
import fr.fgognet.antv.external.editorial.Editorial
import fr.fgognet.antv.external.editorial.EditorialRepository
import fr.fgognet.antv.external.live.LiveRepository
import fr.fgognet.antv.external.nvs.NvsRepository
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel
import fr.fgognet.antv.view.cardList.CardListViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "ANTV/LiveViewModel"


class LiveViewModel(application: Application) :
    AbstractCardListViewModel<LiveCardData>(application) {


    override fun loadCardData(params: Bundle?, force: Boolean) {
        if (super.cardListData.value?.title != null && !force) {
            return
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val editorial: Editorial = try {
                    EditorialRepository.getEditorialInformation()
                } catch (e: Exception) {
                    Editorial(
                        getApplication<Application>().resources.getString(R.string.fail_load_data),
                        "",
                        null
                    )
                }

                withContext(Dispatchers.Main) {
                    Log.i(TAG, "dispatching regenerated view")

                    _cardListData.value =
                        CardListViewData(generateCardData(editorial), editorial.titre)
                }
            }
        }
    }

    private fun generateCardData(editorial: Editorial): List<LiveCardData> {
        Log.v(TAG, "generateCardData")
        val result = arrayListOf<LiveCardData>()
        if (editorial.diffusions == null) {
            return result
        }
        viewModelScope.launch {
            var liveInformation: Map<Int, String>
            withContext(Dispatchers.IO) {
                liveInformation = LiveRepository.getLiveInformation()
                withContext(Dispatchers.Main) {
                    for (diffusion in editorial.diffusions!!) {
                        val cardData = LiveCardData(
                            diffusion.libelle
                                ?: getApplication<Application>().resources.getString(R.string.no_title_broadcast),
                            diffusion.lieu ?: "",
                            diffusion.sujet?.replace("<br>", "\n") ?: "",
                            if (diffusion.id_organe != null) "https://videos.assemblee-nationale.fr/live/images/" + diffusion.id_organe + ".jpg" else "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                            "",
                            diffusion.getFormattedHour(),
                            0,
                            null,
                            false
                        )
                        if (!liveInformation.containsKey(diffusion.flux)) {
                            result.add(cardData)
                        } else {
                            withContext(Dispatchers.IO) {
                                val nvs = NvsRepository.getNvsByCode(
                                    liveInformation[diffusion.flux]!!
                                )
                                withContext(Dispatchers.Main) {
                                    if (liveInformation.containsKey(diffusion.flux) && diffusion.uid_referentiel == nvs.getMeetingID()
                                    ) {
                                        cardData.buttonLabel =
                                            getApplication<Application>().resources.getString(R.string.card_button_label_live)
                                        cardData.buttonBackgroundColorId = android.R.attr.colorError
                                        cardData.clickable = true
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
        }
        return result
    }

}