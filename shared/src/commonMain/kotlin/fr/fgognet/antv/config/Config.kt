package fr.fgognet.antv.config

import io.ktor.client.*
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler

enum class Environment {
    NOTHING, // no current stream
    FIXED, // fixed on 13 july 2022
    REAL_TIME // current live
}

object Config {
    val currentEnvironment = Environment.REAL_TIME
}


expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient


val no_handler: UnknownChildHandler =
    UnknownChildHandler { input, inputKind, descriptor, name, candidates ->
        emptyList()
    }

