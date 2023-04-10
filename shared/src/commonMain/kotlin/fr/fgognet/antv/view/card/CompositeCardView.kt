package fr.fgognet.antv.view.card

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.fgognet.antv.widget.AsyncImage

data class GenericCardData(
    var title: String,
    var subTitle: String?,
    var description: String,
    var buttonName: String,
    var imageCode: String,
    var buttonColor: Color,
    var buttonTextColor: Color,
    var enableButton: Boolean,
)

@Composable
fun CompositeCardView(
    data: GenericCardData,
    buttonClicked: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    ElevatedCard(colors = CardDefaults.cardColors()) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                LandscapeCompositeCardView(data = data, buttonClicked = buttonClicked)
            }
            else -> {
                PortraitCompositeCardView(data = data, buttonClicked = buttonClicked)
            }
        }
    }
}

@Composable
fun LandscapeCompositeCardView(
    data: GenericCardData,
    buttonClicked: () -> Unit,
) {
    Row(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.weight(3f)) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(8f)
                    .padding(10.dp),
                placeholder = painterResource(MR.drawable.ic_baseline_image_24),
                model = data.imageCode,
                contentDescription = data.title
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                enabled = data.enableButton,
                onClick = buttonClicked,
                colors = ButtonDefaults.buttonColors(containerColor = data.buttonColor)
            ) {
                Text(text = data.buttonName, color = data.buttonTextColor)
            }
        }
        Column(modifier = Modifier.weight(8f)) {
            Text(
                text = data.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
            if (data.subTitle != null) {
                Text(
                    text = data.subTitle!!,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Text(
                modifier = Modifier.verticalScroll(
                    enabled = true,
                    state = ScrollState(0),
                ),
                color = MaterialTheme.colorScheme.secondary,
                text = data.description,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic
            )
        }
    }

}

@Composable
fun PortraitCompositeCardView(
    data: GenericCardData,
    buttonClicked: () -> Unit,
) {
    Column(modifier = Modifier.padding(8.dp)) {
        AsyncImage(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth(),
            placeholder = painterResource(R.drawable.ic_baseline_image_24),
            model = data.imageCode,
            contentDescription = data.title
        )
        Column(
            modifier = Modifier
                .weight(8f)
                .padding(16.dp)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = data.enableButton,
                onClick = buttonClicked,
                colors = ButtonDefaults.buttonColors(containerColor = data.buttonColor)
            ) {
                Text(text = data.buttonName, color = data.buttonTextColor)
            }
            Text(
                text = data.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            if (data.subTitle != null) {
                Text(
                    text = data.subTitle!!,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Text(
                modifier = Modifier.verticalScroll(
                    enabled = true,
                    state = ScrollState(0),
                ),
                color = MaterialTheme.colorScheme.secondary,
                text = data.description,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic
            )
        }
    }
}


