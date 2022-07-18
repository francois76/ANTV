package fr.fgognet.antv.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import fr.fgognet.antv.Editorial
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.net.URL

object StreamManager {

    val TAG = "StreamManager"


    fun getStreamUrl(liveID: Int, urlID: Int): String {
        return "https://anorigin.vodalys.com/live/live" + liveID + "/stream" + liveID + "_1_" + urlID + ".m3u8"
    }

    fun getLiveImage(image: String): Bitmap {
        return BitmapFactory.decodeStream(
            URL(image).openStream()
        )
    }

    fun getLiveInfos(): Editorial? {
        Log.i(TAG, "Using URL " + NetworkManager.getEditorialUrl())
        val serializer: Serializer = Persister()
        return serializer.read(
            Editorial::class.java, URL(

                NetworkManager.getEditorialUrl()

            ).openStream()
        )
    }
}