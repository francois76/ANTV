package fr.fgognet.antv.config

import io.ktor.client.*
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler

expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient


@OptIn(ExperimentalXmlUtilApi::class)
val no_handler: UnknownChildHandler =
    UnknownChildHandler { _, _, _, _, _ ->
        emptyList()
    }
