package fr.fgognet.antv.view.isPlaying

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.R

@Composable
fun IsPlaying(
    goToCurrentPlaying: () -> Unit,
) {
    val model: IsPlayingViewModel = viewModel(factory = createViewModelFactory {
        IsPlayingViewModel().start()
    }
    )
    val state by model.isPlayingdata.ld().observeAsState()
    IsPlaying(
        state = state,
        goToCurrentPlaying = goToCurrentPlaying
    )
}

@Composable
fun IsPlaying(
    goToCurrentPlaying: () -> Unit,
    state: IsPlayingData?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                goToCurrentPlaying()
            }
    ) {

        Text(
            text = state?.title ?: "",
            modifier = Modifier
        )
        AsyncImage(
            modifier = Modifier
                .width(80.dp),
            model = state?.imageCode,
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
        state = IsPlayingData("", "coucou", "lorem ipsum...")
    )
}