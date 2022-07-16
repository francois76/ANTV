package fr.fgognet.antv.service

import android.util.Log
import fr.fgognet.antv.Editorial
import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.JAXBException
import java.io.File

object StreamManager {


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
        val xmlFile = File("sampledata/data_test.xml")
        Log.w("antv", "file" + xmlFile.toString())
        val jaxbContext: JAXBContext
        try {
            jaxbContext = JAXBContext.newInstance(Editorial::class.java)
            val jaxbUnmarshaller = jaxbContext.createUnmarshaller()
            return jaxbUnmarshaller.unmarshal(xmlFile) as Editorial

        } catch (e: JAXBException) {
            return null
        }
    }
}