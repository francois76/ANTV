package fr.fgognet.antv.view.cardList.replay

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import fr.fgognet.antv.R
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.external.nvs.NvsRepository
import fr.fgognet.antv.view.card.CardAdapter
import fr.fgognet.antv.view.card.CardData
import fr.fgognet.antv.view.cardList.AbstractCardListFragment
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel
import fr.fgognet.antv.view.player.ARG_DESCRIPTION
import fr.fgognet.antv.view.player.ARG_IMAGE_CODE
import fr.fgognet.antv.view.player.ARG_TITLE
import fr.fgognet.antv.view.player.ARG_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "ANTV/ReplayFragment"

data class ReplayCardData(
    override var title: String,
    override var description: String,
    override var imageCode: String,
    var nvsCode: String?

) : CardData()

class ReplayFragment : AbstractCardListFragment<ReplayCardData>() {

    lateinit var replayViewModel: ReplayViewModel


    override fun initViewModelProvider(savedInstanceState: Bundle?): AbstractCardListViewModel<ReplayCardData> {
        replayViewModel = ViewModelProvider(this)[ReplayViewModel::class.java]
        arguments?.let { bundle ->
            EventSearchQueryParams.values().forEach { queryParam ->
                bundle.getString(queryParam.toString()).let { value ->
                    if (value != null) {
                        replayViewModel.searchQueryFields.put(
                            queryParam, "$value",
                        )
                    }
                }
            }
        }
        return replayViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }


    override fun getTitle(): String {
        return resources.getText(R.string.title_replay).toString()
    }

    override fun getResource(): Int {
        return R.layout.fragment_replay
    }

    override fun buildCardAdapter(): CardAdapter<ReplayCardData> {
        return CardAdapter { cardData, subtitleView, buttonView ->
            buttonView.isEnabled = true
            buttonView.text =
                context?.resources?.getString(R.string.card_button_label_replay)
            val background = TypedValue()
            buttonView.setTextColor(Color.WHITE)
            CoroutineScope(Dispatchers.Main).launch {
                var urlReplay = ""
                var subTitle = ""
                var cardButtonColor = 0
                withContext(Dispatchers.IO) {
                    if (cardData.nvsCode != null) {
                        val nvs = NvsRepository.getNvsByCode(
                            cardData.nvsCode!!
                        )
                        urlReplay = nvs.getReplayURL()
                        subTitle = nvs.getSubtitle()
                        cardButtonColor =
                            android.R.attr.colorPrimaryDark // the status is valorized here to ensure the card actualy has the URL
                    }
                    withContext(Dispatchers.Main) {
                        buttonView.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putString(ARG_URL, urlReplay)
                            bundle.putString(ARG_TITLE, cardData.title)
                            bundle.putString(
                                ARG_DESCRIPTION,
                                cardData.description
                            )
                            bundle.putString(
                                ARG_IMAGE_CODE,
                                cardData.imageCode
                            )
                            Navigation.findNavController(it).navigate(R.id.playerFragment, bundle)
                        }
                        context?.theme?.resolveAttribute(
                            cardButtonColor,
                            background,
                            true
                        )
                        buttonView.setBackgroundColor(
                            background.data
                        )
                        subtitleView.text = subTitle
                    }
                }
            }

        }
    }

}