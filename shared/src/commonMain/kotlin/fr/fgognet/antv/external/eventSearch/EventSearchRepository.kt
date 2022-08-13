package fr.fgognet.antv.external.eventSearch

import io.github.aakira.napier.Napier
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

enum class EventSearchQueryParams {
    Date,
    TypeVideo,
    Commission,
    Tag, // not part of the actual research, but it's the actual label of the search
}

object EventSearchRepository {
    private const val TAG = "ANTV/EventSearchRepository"
    private val json = Json { ignoreUnknownKeys = true }

    fun findEventSearchByParams(params: HashMap<EventSearchQueryParams, String>): List<EventSearch> {
        Napier.v("findEventSearchByDate")

        val requestBuilder = HttpRequestBuilder()
        requestBuilder.url {
            protocol = URLProtocol.HTTP
            host = "httpbin.org"
            path("get")
            parameters.append("file", "/10/55/file.zip")
        }

        val response = client.get<String>(requestBuilder)
        val url = "https://videos.assemblee-nationale.fr/php/eventsearch.php?" +
                params.filter { it.key != EventSearchQueryParams.Tag }.map {
                    "${
                        URLEncoder.encode(
                            it.key.toString(),
                            "utf-8"
                        )
                    }=${URLEncoder.encode(it.value, "utf-8")}"
                }.joinToString("&")

        Napier.i("Calling $url")

        val content = "[" + URL(url).readText().split("[")[1].split("]")[0] + "]"
        Napier.i(content)
        return json.decodeFromString(
            ListSerializer(EventSearch.serializer()),
            content
        )

    }
}