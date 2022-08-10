package fr.fgognet.antv.external.eventSearch

import android.util.Log
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.net.URL
import java.net.URLEncoder

enum class EventSearchQueryParams {
    Date,
    TypeVideo,
    Commission,
}

object EventSearchRepository {
    private const val TAG = "ANTV/EventSearchRepository"
    private val json = Json { ignoreUnknownKeys = true }

    fun findEventSearchByParams(params: HashMap<EventSearchQueryParams, String>): List<EventSearch> {
        Log.v(TAG, "findEventSearchByDate")

        val url = "https://videos.assemblee-nationale.fr/php/eventsearch.php?" +
                params.map {
                    "${URLEncoder.encode(it.key.toString())}=${URLEncoder.encode(it.value)}"
                }.joinToString("&")

        Log.i(TAG, "Calling $url")

        val content = "[" + URL(url).readText().split("[")[1].split("]")[0] + "]"
        Log.i(TAG, content)
        return json.decodeFromString(
            ListSerializer(EventSearch.serializer()),
            content
        )

    }
}