import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.navigation.Navigation
import fr.fgognet.antv.R
import fr.fgognet.antv.external.nvs.NvsRepository
import fr.fgognet.antv.view.CardAdapter
import fr.fgognet.antv.view.cardList.replay.ReplayCardData
import fr.fgognet.antv.view.player.ARG_DESCRIPTION
import fr.fgognet.antv.view.player.ARG_IMAGE_CODE
import fr.fgognet.antv.view.player.ARG_TITLE
import fr.fgognet.antv.view.player.ARG_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun buildCardAdapter(context: Context): CardAdapter<ReplayCardData> {
    return CardAdapter { cardData, subtitleView, buttonView ->
        buttonView.isEnabled = true
        buttonView.text =
            context.resources?.getString(R.string.card_button_label_replay)
        val background = TypedValue()
        buttonView.setTextColor(Color.WHITE)
        CoroutineScope(Dispatchers.Main).launch {
            var urlReplay = ""
            var subTitle = ""
            var cardButtonColor = 0
            withContext(Dispatchers.IO) {
                if (cardData.nvsCode != null) {
                    val nvs = NvsRepository.getNvsByCode(
                        cardData.nvsCode!!
                    )
                    urlReplay = nvs.getReplayURL()

                    if (nvs.getTime() != null) {
                        subTitle =
                            LocalDateTime.parse(nvs.getTime().toString())
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    } else {
                        subTitle = ""
                    }
                    cardButtonColor =
                        android.R.attr.colorPrimaryDark // the status is valorized here to ensure the card actualy has the URL
                }
                withContext(Dispatchers.Main) {
                    buttonView.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putString(ARG_URL, urlReplay)
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
                    context.theme?.resolveAttribute(
                        cardButtonColor,
                        background,
                        true
                    )
                    buttonView.setBackgroundColor(
                        background.data
                    )
                    subtitleView.text = subTitle
                }
            }
        }
    }
}