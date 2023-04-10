package fr.fgognet.antv.config


actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient {
    config(this)
}

actual fun stringResource(value: StringResource) {
}