package fr.fgognet.antv.oldViews

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.navigation.Navigation
import fr.fgognet.antv.R
import fr.fgognet.antv.oldViews.player.ARG_DESCRIPTION
import fr.fgognet.antv.oldViews.player.ARG_IMAGE_CODE
import fr.fgognet.antv.oldViews.player.ARG_TITLE
import fr.fgognet.antv.oldViews.player.ARG_URL
import fr.fgognet.antv.view.cardList.live.LiveCardData


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