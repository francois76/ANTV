package fr.fgognet.antv.view.player

import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalTime
import java.time.format.DateTimeFormatter

actual fun Long.toHour(): String {
    return if (this <= 0L) {
        "..."
    } else {
        val current = LocalTime.fromMillisecondOfDay(this.toInt()).toJavaLocalTime()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        current.format(formatter)
    }
}