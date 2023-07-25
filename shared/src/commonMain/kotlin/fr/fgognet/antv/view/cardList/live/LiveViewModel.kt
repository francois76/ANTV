package fr.fgognet.antv.view.cardList.live

import fr.fgognet.antv.MR
import fr.fgognet.antv.external.editorial.Diffusion
import fr.fgognet.antv.external.editorial.Editorial
import fr.fgognet.antv.external.editorial.EditorialRepository
import fr.fgognet.antv.external.live.LiveRepository
import fr.fgognet.antv.external.nvs.NvsRepository
import fr.fgognet.antv.utils.ResourceOrText
import fr.fgognet.antv.utils.cleanDescription
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel
import fr.fgognet.antv.view.cardList.CardListViewData
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val TAG = "ANTV/LiveViewModel"

class LiveViewModel : AbstractCardListViewModel<LiveCardData, Unit>() {

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
            generateCardData(editorial)
        }
    }

    private fun generateCardData(editorial: Editorial) {
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
            _cards.value = CardListViewData(arrayListOf(), ResourceOrText(editorial.titre))
            return
        }
        viewModelScope.launch {
            try {
                val liveInformation: Map<String, String> = LiveRepository.getLiveInformation()
                val liveMeetingIDs = hashMapOf<String, String>()
                liveInformation.forEach { entry ->
                    try {
                        val meetingID = NvsRepository.getNvsByCode(
                            entry.value
                        ).getMeetingID()
                        if (meetingID != null) {
                            liveMeetingIDs[meetingID] = entry.key
                        }
                    } catch (e: Exception) {
                        Napier.e(tag = TAG, message = e.stackTraceToString())
                    }
                }
                withContext(Dispatchers.Main) {
                    for (d in editorial.diffusions) {
                        val diffusion = d as Diffusion
                        val cardData = LiveCardData(
                            title = ResourceOrText(
                                string = diffusion.libelle,
                                res = MR.strings.no_title_broadcast
                            ),
                            subtitle = diffusion.lieu ?: "",
                            description = cleanDescription(diffusion.sujet) ?: "",
                            imageCode = if (diffusion.id_organe != null) "https://videos.assemblee-nationale.fr/live/images/" + diffusion.id_organe + ".jpg" else "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                            url = "",
                            buttonLabel = ResourceOrText(
                                string = diffusion.getFormattedHour()
                            ),
                            isLive = false,
                        )
                        if (liveMeetingIDs[diffusion.uid_referentiel] != null) {
                            cardData.buttonLabel = ResourceOrText(
                                res = MR.strings.card_button_label_live
                            )
                            cardData.isLive = true
                            cardData.url =
                                "https://videos.assemblee-nationale.fr/live/live${liveMeetingIDs[diffusion.uid_referentiel]}/playlist${liveMeetingIDs[diffusion.uid_referentiel]}.m3u8"
                        }
                        result.add(cardData)
                    }

                }

                _cards.value = CardListViewData(result, ResourceOrText(editorial.titre))

            } catch (e: Exception) {
                Napier.e(tag = TAG, message = e.stackTraceToString())
                _cards.value = CardListViewData(
                    arrayListOf(),
                    ResourceOrText(res = MR.strings.live_error)
                )
            }
        }
    }


}