package fr.fgognet.antv.view.replaySearch

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import kotlin.time.Clock.System


@OptIn(ExperimentalMaterial3Api::class)
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
        val state = rememberDatePickerState(
            initialSelectedDateMillis = System.now().toEpochMilliseconds(),
            initialDisplayedMonthMillis = null,
            yearRange = IntRange(2000, 2200),
            initialDisplayMode = DisplayMode.Picker
        )
        DatePicker(
            state = state
        )
        Button(
            onClick = {
                if (state.selectedDateMillis != null) {
                    query(model.makeSearchBundle(state.selectedDateMillis!!))
                }

            },
            content = {
                Text(text = stringResource(resource = MR.strings.buttom_search))
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }

}
