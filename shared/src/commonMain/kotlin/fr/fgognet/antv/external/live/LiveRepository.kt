package fr.fgognet.antv.external.live

import com.soywiz.korio.net.URL
import io.github.aakira.napier.Napier

object LiveRepository {

    private const val TAG = "ANTV/LiveRepository"

    fun getLiveInformation(): Map<Int, String> {
        Napier.v("getLiveInformation")
        val result = HashMap<Int, String>()
        val url = "https://videos.assemblee-nationale.fr/live/live.txt"
        Napier.i("Calling $url")
        for (line in URL(url).readText().reader()
            .readLines()) {
            val k = Integer.parseInt(line.split(" ")[0])
            result[k] = line.split(" ")[1]
        }
        return result
    }
}