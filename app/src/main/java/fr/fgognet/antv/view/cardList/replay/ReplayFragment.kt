package fr.fgognet.antv.view.cardList.replay

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import fr.fgognet.antv.R
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.view.cardList.AbstractCardListFragment
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel
import fr.fgognet.antv.view.cardList.CardData
import fr.fgognet.antv.view.player.ARG_DESCRIPTION
import fr.fgognet.antv.view.player.ARG_IMAGE_CODE
import fr.fgognet.antv.view.player.ARG_TITLE
import fr.fgognet.antv.view.player.ARG_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

private const val TAG = "ANTV/ReplayFragment"

class ReplayFragment : AbstractCardListFragment() {

    lateinit var replayViewModel: ReplayViewModel


    override fun initViewModelProvider(savedInstanceState: Bundle?): AbstractCardListViewModel {
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

    override fun buildCard(
        cardData: CardData,
        buttonView: Button
    ) {
        val scope = CoroutineScope(Dispatchers.Main)
        buttonView.isEnabled = cardData.clickable
        buttonView.text = cardData.buttonLabel
        val background = TypedValue()
        context?.theme?.resolveAttribute(
            cardData.buttonBackgroundColorId,
            background,
            true
        )
        buttonView.setBackgroundColor(
            background.data
        )
        buttonView.setTextColor(Color.WHITE)
        buttonView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ARG_URL, cardData.url)
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
    }
}