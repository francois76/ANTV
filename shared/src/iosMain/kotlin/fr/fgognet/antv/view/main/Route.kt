package fr.fgognet.antv.view.main


actual fun convertRouteData(routeId: Routes, abstract: AbstractRouteData): RouteData {
    return RouteData(
        nameID = abstract.nameID,
        argumentNames = abstract.argumentNames,
        iconName = abstract.iconName
    )
}