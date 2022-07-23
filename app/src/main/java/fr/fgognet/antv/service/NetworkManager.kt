package fr.fgognet.antv.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import fr.fgognet.antv.Editorial
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.net.URL

enum class Environment {
    NOTHING, // no current stream
    FIXED, // fixed on 13 july 2022
    REAL_TIME // current live
}

val TAG = "ANTV/NetworkManager"

object NetworkManager {


    private val currentEnvironment = Environment.REAL_TIME
    private val environments: HashMap<Environment, String> = hashMapOf(
        Environment.NOTHING to
                "https://videos.assemblee-nationale.fr/live/xml/edito_20220714.xml",
        Environment.FIXED to
                "https://videos.assemblee-nationale.fr/live/xml/edito_20220713.xml",
        Environment.REAL_TIME to
                "https://videos.assemblee-nationale.fr/php/getedito.php"
    )

    var imageCodeToBitmap: HashMap<String, Bitmap> = hashMapOf()


    fun getLiveImage(image: String): Bitmap {
        Log.d(TAG, "getLiveImage")
        if (imageCodeToBitmap.contains(image)) {
            return imageCodeToBitmap[image]!!
        }
        return BitmapFactory.decodeStream(
            URL(image).openStream()
        )
    }

    fun getEditorialInfos(): Editorial {
        Log.d(TAG, "getEditorialInfos")
        Log.i(TAG, "Using URL " + environments[currentEnvironment])
        val serializer: Serializer = Persister()
        return serializer.read(
            Editorial::class.java, URL(
                environments[currentEnvironment]
            ).openStream()
        )
    }
}