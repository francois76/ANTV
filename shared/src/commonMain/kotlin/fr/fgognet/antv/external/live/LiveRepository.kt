package fr.fgognet.antv.external.live

import fr.fgognet.antv.config.httpClient
import io.github.aakira.napier.Napier
import io.ktor.client.call.*
import io.ktor.client.request.*

object LiveRepository {

    private const val TAG = "ANTV/LiveRepository"

    suspend fun getLiveInformation(): Map<Int, String> {
        Napier.v("getLiveInformation")
        val client = httpClient()
        val result = HashMap<Int, String>()
        val url = "https://videos.assemblee-nationale.fr/live/live.txt"
        Napier.i("Calling $url")
        for (line in client.request(url).body<String>().lines()) {
            val k = line.split(" ")[0].toInt()
            result[k] = line.split(" ")[1]
        }
        return result
    }
}