package fr.fgognet.antv.external.nvs

import io.github.aakira.napier.Napier
import kotlinx.serialization.Serializer
import org.simpleframework.xml.core.Persister
import java.net.URL

object NvsRepository {
    private const val TAG = "ANTV/NvsRepository"

    fun getNvsByCode(urlCode: String): Nvs {
        Napier.v("getNvsByCode")
        val serializer: Serializer = Persister()
        val url = "https://videos.assemblee-nationale.fr/Datas/an$urlCode/content/data.nvs"
        Napier.i("calling URL $url")
        val result = serializer.read(
            Nvs::class.java, URL(
                url
            ).openStream()
        )
        Napier.d("getting result $result")
        return result
    }


}