package fr.fgognet.antv.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import fr.fgognet.antv.Diffusion
import fr.fgognet.antv.Editorial
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.net.URL


val TAG = "ANTV/StreamManager"

object StreamManager {

    fun getLiveImage(image: String): Bitmap {
        Log.d(TAG, "getLiveImage")
        return BitmapFactory.decodeStream(
            URL(image).openStream()
        )
    }

    fun getEditorialInfos(): Editorial? {
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

    fun getLiveButtonLabel(diffusion: Diffusion): String {
        Log.d(TAG, "getLiveButtonLabel: $diffusion")
        if (diffusion.isLive) {
            return "live"
        }
        if (diffusion.heure == null) {
            return ""
        }
        val firstCharacter = diffusion.heure!!.substring(0, 1)
        if (firstCharacter == "0") {
            return diffusion.heure!!.substring(1, 2) + "h" + diffusion.heure!!.substring(2, 4)
        } else {
            return diffusion.heure!!.substring(0, 2) + "h" + diffusion.heure!!.substring(2, 4)
        }
    }
}