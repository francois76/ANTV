package fr.fgognet.antv.view.card

import android.graphics.Color
import android.os.Bundle
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
import fr.fgognet.antv.view.cardList.CardStatus
import fr.fgognet.antv.view.cardList.CardType

class CardAdapter :
    ListAdapter<CardData, CardAdapter.CardViewHolder>(CardDiffCallback) {

    // Describes an item view and its place within the RecyclerView
    class CardViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val cardTitleView: TextView = itemView.findViewById(R.id.card_title)
        private val cardSubtitleView: TextView = itemView.findViewById(R.id.card_subtitle)
        private val cardDescriptionView: TextView = itemView.findViewById(R.id.card_description)
        private val cardImageView: ImageView = itemView.findViewById(R.id.card_image_id)
        private val buttonView = itemView.findViewById<Button>(R.id.live_button)
        
        fun bind(cardData: CardData) {
            cardTitleView.text = cardData.title
            cardSubtitleView.text = cardData.subtitle
            cardDescriptionView.text = cardData.description
            cardImageView.contentDescription = cardData.title
            cardImageView.setImageBitmap(ImageRepository.imageCodeToBitmap[cardData.imageCode])
            if (cardData.cardStatus == CardStatus.LIVE || cardData.cardStatus == CardStatus.REPLAY) {
                val background = TypedValue()
                if (cardData.cardStatus == CardStatus.LIVE) {

                    itemView.context?.theme?.resolveAttribute(
                        android.R.attr.colorError,
                        background,
                        true
                    )
                } else {
                    itemView.context?.theme?.resolveAttribute(
                        android.R.attr.colorPrimaryDark,
                        background,
                        true
                    )
                }
                buttonView.text = cardData.buttonLabel
                buttonView.setBackgroundColor(
                    background.data
                )
                buttonView.isEnabled = true
                buttonView.setTextColor(Color.WHITE)
                buttonView.setOnClickListener {
                    if (cardData.cardType == CardType.PLAYLIST) {
                        Navigation.findNavController(it)
                            .navigate(R.id.replayFragment, cardData.targetBundle)
                    } else {
                        val bundle = Bundle()
                        bundle.putString(fr.fgognet.antv.view.player.ARG_URL, cardData.url)
                        bundle.putString(fr.fgognet.antv.view.player.ARG_TITLE, cardData.title)
                        bundle.putString(
                            fr.fgognet.antv.view.player.ARG_DESCRIPTION,
                            cardData.description
                        )
                        bundle.putString(
                            fr.fgognet.antv.view.player.ARG_IMAGE_CODE,
                            cardData.imageCode
                        )
                        Navigation.findNavController(it).navigate(R.id.playerFragment, bundle)
                    }
                }
            } else {
                buttonView.isEnabled = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_card, parent, false)
        return CardViewHolder(view)
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
        return oldItem.url == newItem.url
    }
}