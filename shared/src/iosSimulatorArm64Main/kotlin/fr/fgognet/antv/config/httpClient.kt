package fr.fgognet.antv.config

import dev.icerock.moko.resources.StringResource
import io.ktor.client.*

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient {
    config(this)
}

actual fun stringResource(value: StringResource) {
}