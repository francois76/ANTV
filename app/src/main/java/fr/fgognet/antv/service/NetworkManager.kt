package fr.fgognet.antv.service

import android.graphics.Bitmap

enum class Environment {
    NOTHING, // no current stream
    FIXED, // fixed on 13 july 2022
    REAL_TIME // current live
}

data class Url(val editorialURL: String, val liveURL: String)

object NetworkManager {


    val currentEnvironment = Environment.REAL_TIME
    val environments: HashMap<Environment, Url> = hashMapOf(
        Environment.NOTHING to Url(
            "https://videos.assemblee-nationale.fr/live/xml/edito_20220714.xml",
            ""
        ),
        Environment.FIXED to Url(
            "https://videos.assemblee-nationale.fr/live/xml/edito_20220713.xml",
            "https://web.archive.org/web/20201104094313/http://videos.assemblee-nationale.fr/live/live.txt"
        ),
        Environment.REAL_TIME to Url(
            "https://videos.assemblee-nationale.fr/php/getedito.php",
            "https://videos.assemblee-nationale.fr/live/live.txt"
        ),
    )

    var imageCodeToBitmap: HashMap<String, Bitmap> = hashMapOf(

    )

    fun getliveURL(): String {
        return environments[currentEnvironment]!!.liveURL
    }

    fun getEditorialUrl(): String {
        return environments[currentEnvironment]!!.editorialURL
    }
}