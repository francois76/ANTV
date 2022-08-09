package fr.fgognet.antv.view.cardList.playlist

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import fr.fgognet.antv.R
import fr.fgognet.antv.external.image.ImageRepository
import fr.fgognet.antv.view.cardList.AbstractCardListFragment
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel
import fr.fgognet.antv.view.cardList.CardData
import fr.fgognet.antv.view.cardList.CardType
import fr.fgognet.antv.view.player.ARG_DESCRIPTION
import fr.fgognet.antv.view.player.ARG_IMAGE_CODE
import fr.fgognet.antv.view.player.ARG_TITLE
import fr.fgognet.antv.view.player.ARG_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        cardTitleView: TextView,
        cardSubtitleView: TextView,
        cardDescriptionView: TextView,
        cardImageView: ImageView,
        buttonView: Button
    ) {
        val scope = CoroutineScope(Dispatchers.Main)
        cardTitleView.text = cardData.title
        cardSubtitleView.text = cardData.subtitle
        cardDescriptionView.text = cardData.description
        cardImageView.contentDescription = cardData.title
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
            if (cardData.cardType == CardType.PLAYLIST) {
                Navigation.findNavController(it)
                    .navigate(R.id.replayFragment, cardData.targetBundle)
            } else {
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
        scope.launch {
            withContext(Dispatchers.IO) {
                val bitmap = ImageRepository.getLiveImage(cardData.imageCode)
                withContext(Dispatchers.Main) {
                    cardImageView.setImageBitmap(bitmap)
                }
            }
        }
    }
}