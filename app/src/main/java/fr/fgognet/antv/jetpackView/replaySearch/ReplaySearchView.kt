package fr.fgognet.antv.jetpackView.replaySearch

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import fr.fgognet.antv.R
import fr.fgognet.antv.view.buildColors


private const val TAG = "ANTV/ReplaySearchView"


private var currentDate: Long = 0L

@Composable
fun ReplaySearchView(
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
/*                    findNavController().navigate(
                        R.id.replayFragment,
                        model?.makeSearchBundle(currentDate)
                    )*/
                },
                content = {
                    Text(text = stringResource(id = R.string.buttom_search))
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

}