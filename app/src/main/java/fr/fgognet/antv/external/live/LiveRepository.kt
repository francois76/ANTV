package fr.fgognet.antv.external.live

import android.util.Log
import java.net.URL

object LiveRepository {

    private const val TAG = "ANTV/LiveRepository"

    fun getLiveInformation(): Map<Int, String> {
        Log.v(TAG, "getLiveInformation")
        val result = HashMap<Int, String>()
        val url = "https://videos.assemblee-nationale.fr/live/live.txt"
        Log.i(TAG, "Calling $url")
        for (line in URL(url).readText().reader()
            .readLines()) {
            val k = Integer.parseInt(line.split(" ")[0])
            result[k] = line.split(" ")[1]
        }
        return result
    }
}