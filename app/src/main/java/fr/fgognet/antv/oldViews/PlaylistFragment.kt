package fr.fgognet.antv.view.cardList.playlist

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import fr.fgognet.antv.oldViews.CardAdapter

private const val TAG = "ANTV/PlaylistFragment"


fun buildCardAdapter(context: Context): CardAdapter<PlaylistCardData> {
    return CardAdapter { cardData, _, buttonView ->
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
    }
}