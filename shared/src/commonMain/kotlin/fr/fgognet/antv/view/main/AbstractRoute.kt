package fr.fgognet.antv.view.main

import dev.icerock.moko.resources.StringResource
import fr.fgognet.antv.MR

enum class Routes(val value: String) {
    LIVE("live"),
    PLAYLIST("playlist"),
    SEARCH("search"),
    REPLAY("replay"),
    PLAYER("player")
}

data class AbstractRouteData(
    val argumentNames: List<String>,
    val nameID: StringResource?,
    val iconName: String?
)

expect class RouteData

private val allRoutes = hashMapOf(
    Routes.LIVE to AbstractRouteData(
        argumentNames = arrayListOf(),
        nameID = MR.strings.menu_live,
        iconName = "ic_baseline_live_tv_24"
    ),
    Routes.PLAYLIST to AbstractRouteData(
        argumentNames = arrayListOf(),
        nameID = MR.strings.menu_playlist,
        iconName = "ic_baseline_ondemand_video_24"
    ),
    Routes.SEARCH to AbstractRouteData(
        argumentNames = arrayListOf(),
        nameID = MR.strings.menu_search,
        iconName = "ic_baseline_search_24"
    ),
    Routes.REPLAY to AbstractRouteData(
        argumentNames = arrayListOf(),
        nameID = MR.strings.title_replay,
        iconName = null
    ),
    Routes.PLAYER to AbstractRouteData(
        argumentNames = arrayListOf("title"),
        nameID = null,
        iconName = null
    ),
)

fun getRoute(routeId: Routes): RouteData? {
    if (allRoutes[routeId] == null) {
        return null
    }
    return convertRouteData(routeId, allRoutes[routeId]!!)
}

expect fun convertRouteData(routeId: Routes, abstract: AbstractRouteData): RouteData