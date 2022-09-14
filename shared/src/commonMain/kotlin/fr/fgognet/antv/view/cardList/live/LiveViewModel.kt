package fr.fgognet.antv.view.cardList.live

import fr.fgognet.antv.external.editorial.Diffusion
import fr.fgognet.antv.external.editorial.Editorial
import fr.fgognet.antv.external.editorial.EditorialRepository
import fr.fgognet.antv.external.live.LiveRepository
import fr.fgognet.antv.external.nvs.NvsRepository
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel
import fr.fgognet.antv.view.cardList.CardListViewData
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val TAG = "ANTV/LiveViewModel"

class NewLiveViewModel : AbstractCardListViewModel<LiveCardData, Unit>() {

    override fun loadCardData(params: Unit) {
        viewModelScope.launch {
            val editorial: Editorial = try {
                EditorialRepository.getEditorialInformation()
            } catch (e: Exception) {
                Napier.e(
                    e.stackTraceToString(),
                    tag = TAG
                )
                Editorial(
                    // getApplication<Application>().resources.getString(R.string.fail_load_data),
                    "failed to load data",
                    "",
                    null,
                )
            }
            Napier.i(
                "dispatching regenerated view",
                tag = TAG
            )
            _cards.value =
                CardListViewData(generateCardData(editorial), editorial.titre)

        }
    }

    private fun generateCardData(editorial: Editorial): List<LiveCardData> {
        Napier.v(
            "generateCardData",
            tag = TAG
        )
        Napier.v(
            "editorial: $editorial",
            tag = TAG
        )
        val result = arrayListOf<LiveCardData>()
        if (editorial.diffusions == null) {
            return result
        }
        viewModelScope.launch {
            val liveInformation: Map<String, String> = LiveRepository.getLiveInformation()
            withContext(Dispatchers.Main) {
                for (d in editorial.diffusions) {
                    val diffusion = d as Diffusion
                    val cardData = LiveCardData(
                        diffusion.libelle
                        // ?: getApplication<Application>().resources.getString(R.string.no_title_broadcast),
                            ?: "no_title",
                        diffusion.lieu ?: "",
                        diffusion.sujet?.replace("<br>", "\n") ?: "",
                        if (diffusion.id_organe != null) "https://videos.assemblee-nationale.fr/live/images/" + diffusion.id_organe + ".jpg" else "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                        "",
                        diffusion.getFormattedHour(),
                        false,
                    )
                    if (!liveInformation.containsKey(diffusion.flux)) {
                        result.add(cardData)
                    } else {

                        val nvs = NvsRepository.getNvsByCode(
                            liveInformation[diffusion.flux]!!
                        )
                        withContext(Dispatchers.Main) {
                            if (liveInformation.containsKey(diffusion.flux) && diffusion.uid_referentiel == nvs.getMeetingID()
                            ) {
                                cardData.buttonLabel =
                                        // getApplication<Application>().resources.getString(R.string.card_button_label_live)
                                    "card_button_label_live"
                                cardData.isLive = true
                                cardData.url =
                                    "https://videos.assemblee-nationale.fr/live/live${diffusion.flux}/playlist${diffusion.flux}.m3u8"
                            }
                            result.add(cardData)
                        }

                    }
                }
            }
        }
        return result
    }
}