package fr.fgognet.antv.external.nvs

import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.net.URL

object NvsManager {
    private const val TAG = "ANTV/NvsManager"

    fun getLiveURL(urlCode: String): String {
        val serializer: Serializer = Persister()
        val result = serializer.read(
            Nvs::class.java, URL(
                "https://videos.assemblee-nationale.fr/php/getedito.php"
            ).openStream()
        )
        return result.files
            .filter { it.title == "source" }
            .map { it.url }
            .filterNotNull()
            .map { it.split("domain1")[1].split("_1.mp4")[0] }
            .map { "https://anorigin.vodalys.com/videos/definst/mp4/ida/domain1/$it.smil/master.m3u8" }
            .first()
    }
}