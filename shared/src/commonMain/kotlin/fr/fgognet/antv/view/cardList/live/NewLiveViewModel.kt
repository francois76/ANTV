package fr.fgognet.antv.view.cardList.live

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.readOnly
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.external.editorial.Editorial
import fr.fgognet.antv.external.editorial.EditorialRepository
import fr.fgognet.antv.external.live.LiveRepository
import fr.fgognet.antv.external.nvs.NvsRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CardListViewData(
    var cards: List<LiveCardData>,
    var title: String?
)

private const val TAG = "ANTV/LiveViewModel"

class NewLiveViewModel : ViewModel() {
    private val _cards: MutableLiveData<CardListViewData> =
        MutableLiveData(CardListViewData(cards = arrayListOf(), title = null))
    val cards: LiveData<CardListViewData> = _cards.readOnly()

    fun start() = apply { loadCardData(false) }

    fun loadCardData(force: Boolean) {
        if (_cards.value.title != null && !force) {
            return
        }
        viewModelScope.launch {
            val editorial: Editorial = try {
                EditorialRepository.getEditorialInformation()
            } catch (e: Exception) {
                Napier.e(
                    e.toString(),
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
        val result = arrayListOf<LiveCardData>()
        if (editorial.diffusions == null) {
            return result
        }
        viewModelScope.launch {
            var liveInformation: Map<Int, String>
            liveInformation = LiveRepository.getLiveInformation()
            withContext(Dispatchers.Main) {
                for (diffusion in editorial.diffusions!!) {
                    val cardData = LiveCardData(
                        diffusion.libelle
                        // ?: getApplication<Application>().resources.getString(R.string.no_title_broadcast),
                            ?: "no_title",
                        diffusion.lieu ?: "",
                        diffusion.sujet?.replace("<br>", "\n") ?: "",
                        if (diffusion.id_organe != null) "https://videos.assemblee-nationale.fr/live/images/" + diffusion.id_organe + ".jpg" else "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                        "",
                        diffusion.getFormattedHour(),
                        false
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