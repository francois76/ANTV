package fr.fgognet.antv.config

import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.DefaultXmlSerializationPolicy

enum class Environment {
    NOTHING, // no current stream
    FIXED, // fixed on 13 july 2022
    REAL_TIME // current live
}

object Config {
    val currentEnvironment = Environment.REAL_TIME
}

@OptIn(ExperimentalXmlUtilApi::class)
object MyPolicy : DefaultXmlSerializationPolicy(false, unknownChildHandler = no_handler)
