package fr.fgognet.antv.widget

import kotlinx.datetime.LocalTime

actual fun Long.toHour(): String {
    return if (this <= 0L) {
        "..."
    } else {
        val current = LocalTime.fromMillisecondOfDay(this.toInt())
        current.toString()
    }
}