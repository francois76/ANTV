package fr.fgognet.antv.config

enum class Environment {
    NOTHING, // no current stream
    FIXED, // fixed on 13 july 2022
    REAL_TIME // current live
}

object Config {
    val currentEnvironment = Environment.REAL_TIME
}