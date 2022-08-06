package fr.fgognet.antv.view.card

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import fr.fgognet.antv.R
import fr.fgognet.antv.external.image.ImageRepository
import fr.fgognet.antv.view.cardList.CardData
import fr.fgognet.antv.view.cardList.CardStatus
import fr.fgognet.antv.view.cardList.CardType

private const val TAG = "ANTV/CardFragment"
private const val ARG_TITLE = "title"
private const val ARG_SUBTITLE = "subtitle"
private const val ARG_DESCRIPTION = "description"
private const val ARG_IMAGE = "image"
private const val ARG_LIVE = "live"
private const val ARG_BUTTON_LABEL = "buttonLabel"
private const val ARG_STATUS = "status"
private const val ARG_TYPE = "type"
private const val ARG_TARGET_BUNDLE = "target_bundle"

/**
 * CardFragment fragment that handle each element representing a card on the homepage
 */
class CardFragment : Fragment() {

    private lateinit var data: CardData

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.data = CardData(
                it.getString(ARG_TITLE) ?: "",
                it.getString(ARG_SUBTITLE) ?: "",
                it.getString(ARG_DESCRIPTION) ?: "",
                it.getString(ARG_IMAGE) ?: "",
                it.getString(ARG_LIVE) ?: "",
                it.getString(ARG_BUTTON_LABEL) ?: "",
                CardStatus.valueOf(it.getString(ARG_STATUS) ?: CardStatus.DISABLED.toString()),
                CardType.valueOf(it.getString(ARG_TYPE) ?: CardType.VIDEO.toString()),
                it.getBundle(ARG_TARGET_BUNDLE)
            )
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "onViewCreated")
        view.findViewById<TextView>(R.id.card_title).text = data.title
        view.findViewById<TextView>(R.id.card_subtitle).text = data.subtitle
        view.findViewById<TextView>(R.id.card_description).text = data.description
        val imageView = view.findViewById<ImageView>(R.id.card_image_id)
        imageView.contentDescription = data.title
        imageView.setImageBitmap(ImageRepository.imageCodeToBitmap[data.imageCode])
        val button = view.findViewById<Button>(R.id.live_button)
        button.text = data.buttonLabel
        if (data.cardStatus == CardStatus.LIVE || data.cardStatus == CardStatus.REPLAY) {
            val background = TypedValue()
            if (data.cardStatus == CardStatus.LIVE) {
                context?.theme?.resolveAttribute(
                    android.R.attr.colorError,
                    background,
                    true
                )
            } else {
                context?.theme?.resolveAttribute(
                    android.R.attr.colorPrimaryDark,
                    background,
                    true
                )
            }

            button.setBackgroundColor(
                background.data
            )
            button.isEnabled = true
            button.setTextColor(Color.WHITE)
            button.setOnClickListener {
                if (data.cardType == CardType.PLAYLIST) {
                    Navigation.findNavController(it)
                        .navigate(R.id.replayFragment, data.targetBundle)
                } else {
                    val bundle = Bundle()
                    bundle.putString(fr.fgognet.antv.view.player.ARG_URL, data.url)
                    bundle.putString(fr.fgognet.antv.view.player.ARG_TITLE, data.title)
                    bundle.putString(fr.fgognet.antv.view.player.ARG_DESCRIPTION, data.description)
                    bundle.putString(fr.fgognet.antv.view.player.ARG_IMAGE_CODE, data.imageCode)
                    Navigation.findNavController(it).navigate(R.id.playerFragment, bundle)
                }
            }
        } else {
            button.isEnabled = false
        }
    }

    override fun onDestroyView() {
        Log.v(TAG, "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.v(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_card, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance(
            cardData: CardData
        ) =
            CardFragment().apply {
                Log.v(TAG, "newInstance")
                arguments = Bundle().apply {
                    putString(ARG_TITLE, cardData.title)
                    putString(ARG_SUBTITLE, cardData.subtitle)
                    putString(ARG_DESCRIPTION, cardData.description)
                    putString(ARG_IMAGE, cardData.imageCode)
                    putString(ARG_LIVE, cardData.url)
                    putString(ARG_BUTTON_LABEL, cardData.buttonLabel)
                    putString(ARG_TYPE, cardData.cardType.toString())
                    putString(ARG_STATUS, cardData.cardStatus.toString())
                    putBundle(ARG_TARGET_BUNDLE, cardData.targetBundle)
                }
            }
    }
}