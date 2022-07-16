package fr.fgognet.antv.service

enum class Environment {
    NOTHING, // no current stream
    FIXED, // fixed on 13 july 2022
    REAL_TIME // current live
}

data class Url(val editorialURL: String, val liveURL: String)

object NetworkManager {


    val currentEnvironment = Environment.FIXED
    val environments: HashMap<Environment, Url> = hashMapOf(
        Environment.NOTHING to Url("", ""),
        Environment.FIXED to Url(
            "",
            "https://web.archive.org/web/20201104094313/http://videos.assemblee-nationale.fr/live/live.txt"
        ),
        Environment.REAL_TIME to Url("", "https://videos.assemblee-nationale.fr/live/live.txt"),
    )

    fun getliveURL(): String {
        return environments[currentEnvironment]!!.liveURL
    }

    fun getEditorialUrl(): String {
        return environments[currentEnvironment]!!.editorialURL
    }
}