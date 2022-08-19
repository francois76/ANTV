package fr.fgognet.antv.config

import io.ktor.client.*
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
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


@OptIn(ExperimentalXmlUtilApi::class)
val no_handler: UnknownChildHandler =
    UnknownChildHandler { _, _, _, _, _ ->
        emptyList()
    }

