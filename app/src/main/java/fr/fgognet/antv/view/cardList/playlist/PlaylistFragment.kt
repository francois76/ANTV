package fr.fgognet.antv.view.cardList.playlist

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import fr.fgognet.antv.R
import fr.fgognet.antv.view.cardList.AbstractCardListFragment
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel
import fr.fgognet.antv.view.cardList.CardData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

private const val TAG = "ANTV/PlaylistFragment"

class PlaylistFragment : AbstractCardListFragment() {


    override fun initViewModelProvider(savedInstanceState: Bundle?): AbstractCardListViewModel {
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
            Navigation.findNavController(it)
                .navigate(R.id.replayFragment, cardData.targetBundle)

        }
    }
}