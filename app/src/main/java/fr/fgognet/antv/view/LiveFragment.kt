package fr.fgognet.antv.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.navigation.Navigation
import fr.fgognet.antv.R
import fr.fgognet.antv.view.cardList.live.LiveCardData
import fr.fgognet.antv.view.player.ARG_DESCRIPTION
import fr.fgognet.antv.view.player.ARG_IMAGE_CODE
import fr.fgognet.antv.view.player.ARG_TITLE
import fr.fgognet.antv.view.player.ARG_URL


/**
 * LiveFragment is the main fragment handle by navigation
 */

private const val TAG = "ANTV/LiveFragment"


class LiveFragment {


    fun buildCardAdapter(context: Context): CardAdapter<LiveCardData> {
        Log.v(TAG, "buildCardAdapter")
        return CardAdapter { cardData, subtitleView, buttonView ->
            subtitleView.text = cardData.subtitle
            buttonView.isEnabled = cardData.isLive
            buttonView.text = cardData.buttonLabel
            if (cardData.isLive) {
                val background = TypedValue()
                context.theme?.resolveAttribute(
                    android.R.attr.colorError,
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
    }

}