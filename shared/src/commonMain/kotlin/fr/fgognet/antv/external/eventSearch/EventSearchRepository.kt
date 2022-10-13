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
    Commission;

}

object EventSearchRepository {
    private const val TAG = "ANTV/EventSearchRepository"
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun findEventSearchByParams(params: HashMap<EventSearchQueryParams, String>): List<EventSearch> {
        Napier.v(
            "findEventSearchByDate",
            tag = TAG
        )

        val requestBuilder = HttpRequestBuilder()
        requestBuilder.url {
            protocol = URLProtocol.HTTPS
            host = "videos.assemblee-nationale.fr"
            path("php/eventsearch.php")
            params.forEach {
                parameters.append(it.key.toString(), it.value)
            }
        }
        requestBuilder.build()
        val client = httpClient()
        Napier.i(
            "Calling ${requestBuilder.build().url}",
            tag = TAG
        )

        val content =
            "[" + client.request(requestBuilder).body<String>().split("[")[1].split("]")[0] + "]"
        Napier.i(
            content,
            tag = TAG
        )
        return json.decodeFromString(
            ListSerializer(EventSearch.serializer()),
            content
        )

    }
}