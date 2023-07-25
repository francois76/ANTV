package fr.fgognet.antv.view.replaySearch

import android.widget.CalendarView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import java.util.Calendar

@Composable
actual fun DatePicker(currentDate: Long, onCurrentDateChange: (date: Long) -> Unit) {
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
                onCurrentDateChange(c.timeInMillis)
            }
        }
    )
}