package fr.fgognet.antv.external.eventSearch

import android.util.Log
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneOffset

object EventSearchManager {
    private const val TAG = "ANTV/EventSearchManager"
    private val json = Json { ignoreUnknownKeys = true }

    fun findEventSearchByDate(time: LocalDateTime): List<EventSearch> {
        Log.v(TAG, "findEventSearchByDate")
        Log.d(TAG, "searching for time: $time")
        val dateMorning =
            LocalDateTime.of(time.year, time.month, time.dayOfMonth, 8, 0).toEpochSecond(
                ZoneOffset.UTC
            )
        val dateEvening =
            LocalDateTime.of(time.year, time.month, time.dayOfMonth, 21, 0).toEpochSecond(
                ZoneOffset.UTC
            )
        val url = "https://videos.assemblee-nationale.fr/php/eventsearch.php?Date=" + dateMorning +
                "-" + dateEvening



        Log.i(TAG, "reading url $url")

        val content = "[" + URL(
            "https://videos.assemblee-nationale.fr/php/eventsearch.php?Date=" + dateMorning +
                    "-" + dateEvening
        ).readText().split("[")[1].split("]")[0] + "]"
        Log.i(TAG, content)
        return json.decodeFromString(
            ListSerializer(EventSearch.serializer()),
            content
        )

    }
}