package fr.fgognet.antv.config

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig


actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient {
    config(this)
}

