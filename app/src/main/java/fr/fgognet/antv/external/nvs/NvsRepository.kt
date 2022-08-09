package fr.fgognet.antv.external.nvs

import android.util.Log
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.net.URL

object NvsRepository {
    private const val TAG = "ANTV/NvsRepository"

    fun getNvsByCode(urlCode: String): Nvs {
        Log.v(TAG, "getNvsByCode")
        val serializer: Serializer = Persister()
        val url = "https://videos.assemblee-nationale.fr/Datas/an$urlCode/content/data.nvs"
        Log.i(TAG, "calling URL $url")
        val result = serializer.read(
            Nvs::class.java, URL(
                url
            ).openStream()
        )
        Log.d(TAG, "getting result $result")
        return result
    }


}