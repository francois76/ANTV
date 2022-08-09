package fr.fgognet.antv.view.cardList.live

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import fr.fgognet.antv.R
import fr.fgognet.antv.view.cardList.AbstractCardListFragment
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel
import fr.fgognet.antv.view.cardList.CardData
import fr.fgognet.antv.view.player.ARG_DESCRIPTION
import fr.fgognet.antv.view.player.ARG_IMAGE_CODE
import fr.fgognet.antv.view.player.ARG_TITLE
import fr.fgognet.antv.view.player.ARG_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


/**
 * LiveFragment is the main fragment handle by navigation
 */

private const val TAG = "ANTV/LiveFragment"

class LiveFragment : AbstractCardListFragment() {
    override fun initViewModelProvider(savedInstanceState: Bundle?): AbstractCardListViewModel {
        return ViewModelProvider(this)[LiveViewModel::class.java]
    }

    override fun getTitle(): String {
        return resources.getText(R.string.title_live).toString()
    }

    override fun getResource(): Int {
        return R.layout.fragment_live
    }

    override fun buildCard(
        cardData: CardData,
        buttonView: Button
    ) {
        val scope = CoroutineScope(Dispatchers.Main)
        buttonView.isEnabled = cardData.clickable
        buttonView.text = cardData.buttonLabel
        if (cardData.buttonBackgroundColorId != 0) {
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
        }
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