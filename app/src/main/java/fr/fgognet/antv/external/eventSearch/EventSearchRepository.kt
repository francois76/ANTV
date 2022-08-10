package fr.fgognet.antv.external.eventSearch

import android.util.Log
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.net.URL
import java.net.URLEncoder
import java.util.*

enum class EventSearchQueryParams {
    Date,
    TypeVideo,
    Commission,
    Tag, // not part of the actual research, but it's the actual label of the search
}

object EventSearchRepository {
    private const val TAG = "ANTV/EventSearchRepository"
    private val json = Json { ignoreUnknownKeys = true }

    fun findEventSearchByParams(params: EnumMap<EventSearchQueryParams, String>): List<EventSearch> {
        Log.v(TAG, "findEventSearchByDate")

        val url = "https://videos.assemblee-nationale.fr/php/eventsearch.php?" +
                params.filter { it.key != EventSearchQueryParams.Tag }.map {
                    "${
                        URLEncoder.encode(
                            it.key.toString(),
                            "utf-8"
                        )
                    }=${URLEncoder.encode(it.value, "utf-8")}"
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