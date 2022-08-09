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
import fr.fgognet.antv.external.image.ImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "ANTV/CardAdapter"

abstract class CardData {
    abstract var title: String
    abstract var description: String
    abstract var imageCode: String
    abstract var buttonLabel: String


}

class CardAdapter<T : CardData>(private val buildCard: (T, TextView, Button) -> Unit) :
    ListAdapter<T, CardAdapter.CardViewHolder<T>>(CardDiffCallback()) {

    // Describes an item view and its place within the RecyclerView
    class CardViewHolder<T : CardData>(
        itemView: View,
        private var buildCard: (T, TextView, Button) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val cardTitleView: TextView = itemView.findViewById(R.id.card_title)
        private val cardSubtitleView: TextView = itemView.findViewById(R.id.card_subtitle)
        private val cardDescriptionView: TextView = itemView.findViewById(R.id.card_description)
        private val cardImageView: ImageView = itemView.findViewById(R.id.card_image_id)
        private val buttonView: Button = itemView.findViewById(R.id.live_button)

        fun bind(cardData: T) {
            cardTitleView.text = cardData.title
            cardDescriptionView.text = cardData.description
            cardImageView.contentDescription = cardData.title
            buildCard(
                cardData,
                cardSubtitleView,
                buttonView
            )
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    val bitmap = ImageRepository.getLiveImage(cardData.imageCode)
                    withContext(Dispatchers.Main) {
                        cardImageView.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder<T> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_card, parent, false)
        return CardViewHolder<T>(view, buildCard)
    }

    override fun onBindViewHolder(holder: CardViewHolder<T>, position: Int) {
        val cardData = getItem(position)
        holder.bind(cardData)
    }

}

class CardDiffCallback<T : CardData> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.title == newItem.title
    }
}