package fr.fgognet.antv.view.isPlaying

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.R

@Composable
fun IsPlaying(
    goToCurrentPlaying: () -> Unit,
    imageCode: String,
    title: String,
    description: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                goToCurrentPlaying()
            }
    ) {

        Text(
            text = title,
            modifier = Modifier
        )
        AsyncImage(
            modifier = Modifier
                .width(80.dp),
            model = imageCode,
            placeholder = painterResource(R.drawable.ic_baseline_live_tv_24),
            contentDescription = ""
        )
        Text(
            text = stringResource(resource = MR.strings.is_playing),
            modifier = Modifier
                .height(20.dp)
        )

    }
}

@Preview(widthDp = 300, heightDp = 100)
@Composable
fun IsPlayingPreview() {
    IsPlaying(
        goToCurrentPlaying = {},
        title = "", imageCode = "coucou", description = "lorem ipsum..."
    )
}