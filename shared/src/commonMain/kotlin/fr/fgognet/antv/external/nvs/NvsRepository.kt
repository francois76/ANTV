package fr.fgognet.antv.external.nvs

import fr.fgognet.antv.config.MyPolicy
import fr.fgognet.antv.config.httpClient
import io.github.aakira.napier.Napier
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.core.XmlVersion
import nl.adaptivity.xmlutil.serialization.XML

object NvsRepository {
    private const val TAG = "ANTV/NvsRepository"

    @OptIn(ExperimentalXmlUtilApi::class)
    suspend fun getNvsByCode(urlCode: String): Nvs {
        Napier.v(
            "getNvsByCode",
            tag = TAG
        )
        val client = httpClient()
        val url = "https://videos.assemblee-nationale.fr/Datas/an/$urlCode/content/data.nvs"
        Napier.i(
            "calling URL $url",
            tag = TAG
        )
        val resultString =
            client.request(url)
                .body<String>()
        val format = XML {
            xmlVersion = XmlVersion.XML10
            xmlDeclMode = XmlDeclMode.Charset
            policy = MyPolicy

        }
        return format.decodeFromString(resultString)
    }


}