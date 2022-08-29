package fr.fgognet.antv.view.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.soywiz.korim.format.toAndroidBitmap
import fr.fgognet.antv.view.buildColors


@Composable
fun cardView(data: CardData, model: CardViewModel?) {
    MaterialTheme(colorScheme = buildColors(context = LocalContext.current)) {
        Card {
            Column {
                if (model?.image?.value != null) {
                    Image(
                        bitmap = model.image.value!!.toAndroidBitmap().asImageBitmap(),
                        contentDescription = data.description
                    )
                }
                Column {
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = data.title)
                        Text(text = "")
                        Text(
                            modifier = Modifier.scrollable(
                                ScrollableState { 0F },
                                enabled = true,
                                orientation = Orientation.Vertical,
                            ),
                            text = data.description
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun cardViewPreview() {
    // cardView()
}