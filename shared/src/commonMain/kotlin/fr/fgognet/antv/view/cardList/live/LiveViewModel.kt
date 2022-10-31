package fr.fgognet.antv.view.cardList.live

import fr.fgognet.antv.MR
import fr.fgognet.antv.external.editorial.Diffusion
import fr.fgognet.antv.external.editorial.Editorial
import fr.fgognet.antv.external.editorial.EditorialRepository
import fr.fgognet.antv.external.live.LiveRepository
import fr.fgognet.antv.external.nvs.NvsRepository
import fr.fgognet.antv.utils.ResourceOrText
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
                withContext(Dispatchers.Main) {
                    for (d in editorial.diffusions) {
                        try {
                            val diffusion = d as Diffusion
                            val cardData = LiveCardData(
                                title = ResourceOrText(
                                    string = diffusion.libelle,
                                    stringResource = MR.strings.no_title_broadcast
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
                            if (!liveInformation.containsKey(diffusion.flux)) {
                                result.add(cardData)
                            } else {
                                val nvs = NvsRepository.getNvsByCode(
                                    liveInformation[diffusion.flux]!!
                                )
                                withContext(Dispatchers.Main) {
                                    if (liveInformation.containsKey(diffusion.flux) && diffusion.uid_referentiel == nvs.getMeetingID()
                                    ) {
                                        cardData.buttonLabel = ResourceOrText(
                                            stringResource = MR.strings.card_button_label_live
                                        )
                                        cardData.isLive = true
                                        cardData.url =
                                            "https://videos.assemblee-nationale.fr/live/live${diffusion.flux}/playlist${diffusion.flux}.m3u8"
                                    }
                                    result.add(cardData)
                                }
                            }
                        } catch (e: Exception) {
                            Napier.e(tag = TAG, message = e.stackTraceToString())
                            result.add(
                                LiveCardData(
                                    title = ResourceOrText(stringResource = MR.strings.live_error),
                                    description = "",
                                    buttonLabel = ResourceOrText(),
                                    imageCode = "",
                                    isLive = false,
                                    subtitle = "",
                                    url = ""
                                )
                            )
                        }
                    }

                    _cards.value = CardListViewData(result, ResourceOrText(editorial.titre))
                }
            } catch (e: Exception) {
                Napier.e(tag = TAG, message = e.stackTraceToString())
                _cards.value = CardListViewData(
                    arrayListOf(),
                    ResourceOrText(stringResource = MR.strings.live_error)
                )
            }
        }
    }


    companion object {
        fun cleanDescription(rawDescription: String?): String? {
            if (rawDescription == null) {
                return null
            }
            // base cleaning of description
            var result = rawDescription.replace("<br>", "\n").replace("–", "-").trim()
            // replace line separator ;-
            if (result != "" && "-" == result.subSequence(0, 1)) {
                result = result.replace(";-", "\n-")
            }
            // replace end of line separator ;
            result = result.replace(";\n", "\n")
            // if ; is used as line separator, it is replaced
            if (result.split("\n-").size == 1) {
                result = result.replace(";", "\n- ")
            }
            return result
        }
    }
}