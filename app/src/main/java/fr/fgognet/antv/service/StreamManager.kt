package fr.fgognet.antv.service

import android.util.Log
import fr.fgognet.antv.Editorial
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.net.URL

object StreamManager {

    val TAG = "StreamManager"

    val streamURLMap: HashMap<Int, String> = hashMapOf(
        42 to "https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8",
        43 to "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
        44 to getStreamUrl(44, 1)
    )

    fun getOriginalStreamUrl(): String? {
        return streamURLMap[42]
    }

    fun getStreamUrl(liveID: Int, urlID: Int): String {
        return "https://anorigin.vodalys.com/live/live" + liveID + "/stream" + liveID + "_1_" + urlID + ".m3u8"
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