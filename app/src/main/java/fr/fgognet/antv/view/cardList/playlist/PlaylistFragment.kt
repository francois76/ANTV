package fr.fgognet.antv.view.cardList.playlist

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import fr.fgognet.antv.R
import fr.fgognet.antv.view.card.CardAdapter
import fr.fgognet.antv.view.card.CardData
import fr.fgognet.antv.view.cardList.AbstractCardListFragment
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel

private const val TAG = "ANTV/PlaylistFragment"

data class PlaylistCardData(
    override var title: String,
    var subtitle: String,
    override var description: String,
    override var imageCode: String,
    override var buttonLabel: String,
    var targetBundle: Bundle?,

    ) : CardData()

class PlaylistFragment : AbstractCardListFragment<PlaylistCardData>() {


    override fun initViewModelProvider(savedInstanceState: Bundle?): AbstractCardListViewModel<PlaylistCardData> {
        return ViewModelProvider(this)[PlaylistViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun getTitle(): String {
        return resources.getText(R.string.title_replay).toString()
    }

    override fun getResource(): Int {
        return R.layout.fragment_playlist
    }

    override fun buildCardAdapter(): CardAdapter<PlaylistCardData> {
        return CardAdapter { cardData, subtitleView, buttonView ->
            subtitleView.text = cardData.subtitle
            buttonView.isEnabled = true
            buttonView.text = cardData.buttonLabel
            val background = TypedValue()
            context?.theme?.resolveAttribute(
                android.R.attr.colorPrimaryDark,
                background,
                true
            )
            buttonView.setBackgroundColor(
                background.data
            )
            buttonView.setTextColor(Color.WHITE)
            buttonView.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.replayFragment, cardData.targetBundle)

            }
        }
    }
}