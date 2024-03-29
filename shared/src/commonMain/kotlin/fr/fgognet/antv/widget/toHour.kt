package fr.fgognet.antv.widget

import kotlinx.datetime.LocalTime

fun Long.toHour(): String {
    return if (this <= 0L) {
        "..."
    } else {
        val current = LocalTime.fromMillisecondOfDay(this.toInt())
        return "${current.hour}".padStart(2, '0') + ":" + "${current.minute}".padStart(
            2,
            '0'
        ) + ":" + "${current.second}".padStart(2, '0')
    }
}

