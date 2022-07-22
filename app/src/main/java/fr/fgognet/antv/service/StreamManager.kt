package fr.fgognet.antv.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import fr.fgognet.antv.Editorial
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.net.URL


val TAG = "ANTV/StreamManager"

object StreamManager {

    fun getLiveImage(image: String): Bitmap {
        Log.d(TAG, "getLiveImage")
        if (NetworkManager.imageCodeToBitmap.contains(image)) {
            return NetworkManager.imageCodeToBitmap[image]!!
        }
        return BitmapFactory.decodeStream(
            URL(image).openStream()
        )
    }

    fun getEditorialInfos(): Editorial {
        Log.d(TAG, "getEditorialInfos")
        Log.i(TAG, "Using URL " + NetworkManager.getEditorialUrl())
        val serializer: Serializer = Persister()
        return serializer.read(
            Editorial::class.java, URL(
                NetworkManager.getEditorialUrl()
            ).openStream()
        )
    }

    fun getLiveInfos(): List<Int> {
        Log.d(TAG, "getLiveInfos")
        Log.i(TAG, "Using URL " + NetworkManager.getliveURL())
        val result = ArrayList<Int>()
        for (line in URL(NetworkManager.getliveURL()).readText().reader().readLines()) {
            val k = Integer.parseInt(line.split(" ")[0])
            result.add(k)
        }
        return result
    }

    fun getLiveButtonLabel(isLive: Boolean, hour: String): String {
        if (isLive) {
            return "live"
        }
        if (hour == "") {
            return ""
        }

        val firstCharacter = hour.substring(0, 1)
        if (firstCharacter == "0") {
            return hour.substring(1, 2) + "h" + hour.substring(2, 4)
        } else {
            return hour.substring(0, 2) + "h" + hour.substring(2, 4)
        }
    }
}