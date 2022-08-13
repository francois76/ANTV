package fr.fgognet.antv.external.nvs

import fr.fgognet.antv.config.httpClient
import io.github.aakira.napier.Napier
import io.ktor.client.call.*
import io.ktor.client.request.*

object NvsRepository {
    private const val TAG = "ANTV/NvsRepository"

    suspend fun getNvsByCode(urlCode: String): Nvs {
        Napier.v("getNvsByCode")
        val client = httpClient()
        val url = "https://videos.assemblee-nationale.fr/Datas/an$urlCode/content/data.nvs"
        Napier.i("calling URL $url")
        return client.request(url)
            .body()
    }


}