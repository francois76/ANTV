package fr.fgognet.antv.config

import io.ktor.client.*

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient {
    config(this)
}
