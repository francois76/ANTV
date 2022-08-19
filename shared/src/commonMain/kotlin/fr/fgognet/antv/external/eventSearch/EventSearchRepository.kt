package fr.fgognet.antv.external.eventSearch

import fr.fgognet.antv.config.httpClient
import io.github.aakira.napier.Napier
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
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

    suspend fun findEventSearchByParams(params: HashMap<EventSearchQueryParams, String>): List<EventSearch> {
        Napier.v("findEventSearchByDate")

        val requestBuilder = HttpRequestBuilder()
        requestBuilder.url {
            protocol = URLProtocol.HTTPS
            host = "videos.assemblee-nationale.fr"
            path("php/eventsearch.php")
            parameters.append("file", "/10/55/file.zip")
            params.filter { it.key != EventSearchQueryParams.Tag }.forEach {
                parameters.append(it.key.toString(), it.value)
            }
        }
        requestBuilder.build()
        val client = httpClient()
        Napier.i("Calling ${requestBuilder.build().url}")

        val content =
            "[" + client.request(requestBuilder).body<String>().split("[")[1].split("]")[0] + "]"
        Napier.i(content)
        return json.decodeFromString(
            ListSerializer(EventSearch.serializer()),
            content
        )

    }
}