package fr.fgognet.antv.view.replaySearch

import android.icu.util.Calendar
import android.widget.CalendarView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.resources.compose.stringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.view.utils.buildColors


private var currentDate: Long = 0L

@Composable
fun ReplaySearchView(
    model: ReplaySearchViewModel = viewModel(
        factory = createViewModelFactory {
            ReplaySearchViewModel().start()
        }
    ),
    query: (queryParams: Unit) -> Unit
) {
    MaterialTheme(colorScheme = buildColors(context = LocalContext.current)) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = Dp(20F))
        ) {
            lateinit var calendarView: CalendarView
            AndroidView(
                {
                    calendarView = CalendarView(it)
                    calendarView
                },
                modifier = Modifier
                    .fillMaxWidth(),
                update = { views ->
                    views.setOnDateChangeListener { _, year, month, day ->
                        val c: Calendar = Calendar.getInstance()
                        c.set(year, month, day)
                        currentDate = c.timeInMillis
                    }
                }
            )
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

}