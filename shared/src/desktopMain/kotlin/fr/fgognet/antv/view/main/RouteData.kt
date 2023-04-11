package fr.fgognet.antv.view.main

import dev.icerock.moko.resources.StringResource


actual class NamedNavArgument
actual class NavDeepLink
actual class RouteData actual constructor(
    id: String,
    nameID: StringResource?,
    iconName: String?,
    argumentsRaw: List<String>?,
    deepLinksRaw: List<String>?
) : RouteDataRaw()