package fr.fgognet.antv.view.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.fgognet.antv.R
import fr.fgognet.antv.view.cardList.CardData

private const val TAG = "ANTV/CardAdapter"

class CardAdapter(private val buildCard: (CardData, TextView, TextView, TextView, ImageView, Button) -> Unit) :
    ListAdapter<CardData, CardAdapter.CardViewHolder>(CardDiffCallback) {

    // Describes an item view and its place within the RecyclerView
    class CardViewHolder(
        itemView: View,
        private var buildCard: (CardData, TextView, TextView, TextView, ImageView, Button) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val cardTitleView: TextView = itemView.findViewById(R.id.card_title)
        private val cardSubtitleView: TextView = itemView.findViewById(R.id.card_subtitle)
        private val cardDescriptionView: TextView = itemView.findViewById(R.id.card_description)
        private val cardImageView: ImageView = itemView.findViewById(R.id.card_image_id)
        private val buttonView: Button = itemView.findViewById(R.id.live_button)

        fun bind(data: CardData) {
            buildCard(
                data,
                cardTitleView,
                cardSubtitleView,
                cardDescriptionView,
                cardImageView,
                buttonView
            )
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_card, parent, false)
        return CardViewHolder(view, buildCard)
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