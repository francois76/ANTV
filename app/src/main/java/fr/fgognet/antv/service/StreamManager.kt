package fr.fgognet.antv.service

import android.util.Log
import fr.fgognet.antv.Editorial
import java.net.URL
import java.net.URLEncoder
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException

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

        val jaxbContext: JAXBContext
        try {
            jaxbContext = JAXBContext.newInstance(Editorial::class.java)
            val jaxbUnmarshaller = jaxbContext.createUnmarshaller()
            return jaxbUnmarshaller.unmarshal(
                URL(
                    URLEncoder.encode(
                        NetworkManager.getEditorialUrl(),
                        "UTF-8"
                    )
                ).openStream()
            ) as Editorial

        } catch (e: JAXBException) {
            e.printStackTrace()
            return Editorial("Network exception", e.stackTraceToString())
        }

    }
}