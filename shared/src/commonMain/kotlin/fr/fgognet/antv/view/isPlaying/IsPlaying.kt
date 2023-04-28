package fr.fgognet.antv.view.isPlaying

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.widget.AsyncImage
import fr.fgognet.antv.widget.orientationWrapper

@Composable
fun IsPlaying(goToCurrentPlaying: () -> Unit, model: IsPlayingViewModel) {
    val state by model.isPlayingData.observeAsState()
    if (state.hasPlayingData) {
        IsPlayingState(
            goToCurrentPlaying = goToCurrentPlaying,
            imageCode = state.imageCode,
            title = state.title,
            description = state.description
        )
    }
}

@Composable
fun IsPlayingState(
    goToCurrentPlaying: () -> Unit,
    imageCode: String,
    title: String,
    description: String,
) {
    orientationWrapper(
        portrait = {
            IsPlayingStatePortrait(
                goToCurrentPlaying = goToCurrentPlaying,
                imageCode = imageCode,
                title = title,
                description = description
            )
        }, landscape = {
            IsPlayingStateLandscape(
                goToCurrentPlaying = goToCurrentPlaying,
                imageCode = imageCode,
                title = title
            )
        }
    )

}

@Composable
fun IsPlayingStatePortrait(
    goToCurrentPlaying: () -> Unit,
    imageCode: String,
    title: String,
    description: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable {
                goToCurrentPlaying()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(60.dp),
                    model = imageCode,
                    placeholder = painterResource(MR.images.ic_baseline_image_24),
                    contentDescription = ""
                )
            }
            Column(modifier = Modifier.padding(2.dp)) {
                Text(
                    text = stringResource(resource = MR.strings.is_playing),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .height(20.dp)
                )
                Text(
                    text = title,
                    modifier = Modifier
                )
                Text(
                    text = description,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun IsPlayingStateLandscape(
    goToCurrentPlaying: () -> Unit,
    imageCode: String,
    title: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable {
                goToCurrentPlaying()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxHeight()) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight(),
                    model = imageCode,
                    placeholder = painterResource(MR.images.ic_baseline_image_24),
                    contentDescription = ""
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(resource = MR.strings.is_playing),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .height(20.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                )
            }
        }
    }
}

