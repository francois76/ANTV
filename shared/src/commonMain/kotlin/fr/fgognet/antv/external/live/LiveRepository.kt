package fr.fgognet.antv.external.live

import fr.fgognet.antv.config.httpClient
import io.github.aakira.napier.Napier
import io.ktor.client.call.*
import io.ktor.client.request.*

object LiveRepository {

    private const val TAG = "ANTV/LiveRepository"

    suspend fun getLiveInformation(): Map<String, String> {
        Napier.v(
            "getLiveInformation",
            tag = TAG
        )
        val client = httpClient()
        val result = HashMap<String, String>()
        val url = "https://videos.assemblee-nationale.fr/live/live.txt"
        Napier.i(
            "Calling $url",
            tag = TAG
        )
        for (line in client.request(url).body<String>().lines()) {
            val lineElements = line.split(" ")
            if (lineElements.size == 2) {
                result[lineElements[0]] = lineElements[1]
            }
        }
        return result
    }
}