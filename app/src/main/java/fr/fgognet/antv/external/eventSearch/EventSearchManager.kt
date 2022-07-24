package fr.fgognet.antv.external.eventSearch

import android.util.Log
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneOffset

object EventSearchManager {
    private const val TAG = "ANTV/EventSearchManager"
    fun findEventSearchByDate(time: LocalDateTime): List<EventSearch> {
        val dateMorning =
            LocalDateTime.of(time.year, time.month, time.dayOfMonth, 8, 0).toEpochSecond(
                ZoneOffset.UTC
            )
        val dateEvening =
            LocalDateTime.of(time.year, time.month, time.dayOfMonth, 23, 0).toEpochSecond(
                ZoneOffset.UTC
            )
        val url = "https://videos.assemblee-nationale.fr/php/eventsearch.php?Date=" + dateMorning +
                "-" + dateEvening
        val reader = BufferedReader(
            URL(
                "https://videos.assemblee-nationale.fr/php/eventsearch.php?Date=" + dateMorning +
                        "-" + dateEvening
            ).openStream().reader()
        )
        val content: String
        Log.i(TAG, "reading url $url")
        reader.use {
            content = it.readText()
        }
        Log.i(TAG, content)
        return Json.decodeFromString(content)

    }
}