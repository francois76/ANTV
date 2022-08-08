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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.fgognet.antv.R
import fr.fgognet.antv.external.image.ImageRepository
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

private const val TAG = "ANTV/CardAdapter"

class CardAdapter(private val loadRemoteCardData: (CardData) -> CardData) :
    ListAdapter<CardData, CardAdapter.CardViewHolder>(CardDiffCallback) {

    // Describes an item view and its place within the RecyclerView
    class CardViewHolder(itemView: View, private var loadRemoteCardData: (CardData) -> CardData) :
        RecyclerView.ViewHolder(itemView) {

        private val cardTitleView: TextView = itemView.findViewById(R.id.card_title)
        private val cardSubtitleView: TextView = itemView.findViewById(R.id.card_subtitle)
        private val cardDescriptionView: TextView = itemView.findViewById(R.id.card_description)
        private val cardImageView: ImageView = itemView.findViewById(R.id.card_image_id)
        private val buttonView = itemView.findViewById<Button>(R.id.live_button)

        fun bind(data: CardData) {
            val scope = CoroutineScope(Dispatchers.Main)
            val cardData = loadRemoteCardData(data)
            cardTitleView.text = cardData.title
            cardSubtitleView.text = cardData.subtitle
            cardDescriptionView.text = cardData.description
            cardImageView.contentDescription = cardData.title
            buttonView.isEnabled = cardData.clickable
            buttonView.text = cardData.buttonLabel
            if (cardData.buttonBackgroundColorId != 0) {
                val background = TypedValue()
                itemView.context?.theme?.resolveAttribute(
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
                    Log.w(TAG, "fetched bitmap :" + cardData.imageCode)
                    withContext(Dispatchers.Main) {
                        ImageRepository.imageCodeToBitmap[cardData.imageCode] =
                            bitmap
                        cardData.imageBitmap = bitmap
                        cardImageView.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_card, parent, false)
        return CardViewHolder(view, loadRemoteCardData)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardData = getItem(position)
        holder.bind(cardData)
    }

}

object CardDiffCallback : DiffUtil.ItemCallback<CardData>() {
    override fun areItemsTheSame(oldItem: CardData, newItem: CardData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CardData, newItem: CardData): Boolean {
        return oldItem.title == newItem.title
    }
}