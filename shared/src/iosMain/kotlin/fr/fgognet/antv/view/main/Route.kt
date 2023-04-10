package fr.fgognet.antv.view.main

import dev.icerock.moko.resources.StringResource

actual class RouteData(
    val argumentNames: List<String>,
    val nameID: StringResource?,
    val iconName: String?
)

actual fun convertRouteData(routeId: Routes, abstract: AbstractRouteData): RouteData {
    return RouteData(
        nameID = abstract.nameID,
        argumentNames = abstract.argumentNames,
        iconName = abstract.iconName
    )
}