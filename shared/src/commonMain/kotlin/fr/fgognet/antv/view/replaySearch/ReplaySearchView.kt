package fr.fgognet.antv.view.replaySearch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR


private var currentDate: Long = 0L

@Composable
fun ReplaySearchView(
    model: ReplaySearchViewModel = getViewModel(factory = viewModelFactory {
        ReplaySearchViewModel().start()
    }, key = "ReplaySearchViewModel"),
    query: (queryParams: Unit) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = Dp(20F))
    ) {
        DatePicker(currentDate = currentDate, onCurrentDateChange = { date ->
            currentDate = date
        })
        Button(
            onClick = {
                query(model.makeSearchBundle(currentDate))
            },
            content = {
                Text(text = stringResource(resource = MR.strings.buttom_search))
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }

}

@Composable
expect fun DatePicker(currentDate: Long, onCurrentDateChange: (date: Long) -> Unit)

