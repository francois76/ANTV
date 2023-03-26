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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.R

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
                    placeholder = painterResource(R.drawable.ic_baseline_image_24),
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
                    placeholder = painterResource(R.drawable.ic_baseline_image_24),
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

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 400, heightDp = 50, name = "landscape")
@Composable
fun IsPlayingPreviewLandscape() {
    IsPlayingStateLandscape(
        goToCurrentPlaying = {},
        imageCode = "coucou", title = "montitre"
    )
}

@Preview(widthDp = 300, heightDp = 100, name = "portrait")
@Composable
fun IsPlayingPreviewPortrait() {
    IsPlayingStatePortrait(
        goToCurrentPlaying = {},
        title = "montitre", imageCode = "coucou", description = """
        Lorem ipsum dolor sit amet. Et molestiae illo non dolor At ipsa voluptas ex voluptas asperiores ad repudiandae enim eos veritatis eveniet. Aut voluptatum obcaecati At quis maxime ea aliquam consectetur sit error blanditiis.

        Quo repellendus laborum in atque vitae et tempore corporis ut consequatur consectetur quo debitis dignissimos. Cum dicta fugiat ut autem accusantium et ipsa modi. Ea corrupti quidem et magni voluptas est sunt delectus id deleniti dolores! Cum eveniet soluta et rerum repellat ut dolor magni in internos quia.

        Eos velit repellendus id saepe voluptatem eum tempore enim. Ea perspiciatis sapiente est voluptate nihil aut aliquid doloremque vel fugiat dignissimos qui laboriosam praesentium id culpa nemo sit distinctio. Quo autem consectetur vel nisi dolor aperiam sapiente.
        
    """
    )
}