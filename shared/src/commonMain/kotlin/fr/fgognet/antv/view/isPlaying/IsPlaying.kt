package fr.fgognet.antv.view.isPlaying

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.widget.AsyncImage

@Composable
fun IsPlaying(goToCurrentPlaying: () -> Unit, model: IsPlayingViewModel) {
    val state by model.isPlayingData.ld().observeAsState()
    if (state != null && state?.hasPlayingData == true) {
        IsPlayingState(
            goToCurrentPlaying = goToCurrentPlaying,
            imageCode = state!!.imageCode,
            title = state!!.title,
            description = state!!.description
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
    val configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        IsPlayingStateLandscape(
            goToCurrentPlaying = goToCurrentPlaying,
            imageCode = imageCode,
            title = title
        )
    } else {
        IsPlayingStatePortrait(
            goToCurrentPlaying = goToCurrentPlaying,
            imageCode = imageCode,
            title = title,
            description = description
        )
    }
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

