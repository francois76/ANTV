package fr.fgognet.antv.view.cardList.playlist

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.navigation.Navigation
import fr.fgognet.antv.R
import fr.fgognet.antv.oldViews.CardAdapter

private const val TAG = "ANTV/PlaylistFragment"


fun buildCardAdapter(context: Context): CardAdapter<PlaylistCardData> {
    return CardAdapter { cardData, _, buttonView ->
        buttonView.isEnabled = true
        buttonView.text = context.resources?.getString(R.string.card_button_label_playlist)
        val background = TypedValue()
        context.theme?.resolveAttribute(
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
                .navigate(R.id.replayFragment, cardData.targetBundle as Bundle)

        }
    }
}