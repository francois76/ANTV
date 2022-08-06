package fr.fgognet.antv.external.eventSearch

import android.util.Log
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.net.URL

enum class EventSearchQueryParams {
    Date,
}

object EventSearchRepository {
    private const val TAG = "ANTV/EventSearchRepository"
    private val json = Json { ignoreUnknownKeys = true }

    fun findEventSearchByParams(params: HashMap<EventSearchQueryParams, String>): List<EventSearch> {
        Log.v(TAG, "findEventSearchByDate")

        var url = "https://videos.assemblee-nationale.fr/php/eventsearch.php?"
        params.forEach {
            url += "${it.key}=${it.value}"
        }

        Log.i(TAG, "Calling $url")

        val content = "[" + URL(url).readText().split("[")[1].split("]")[0] + "]"
        Log.i(TAG, content)
        return json.decodeFromString(
            ListSerializer(EventSearch.serializer()),
            content
        )

    }
}