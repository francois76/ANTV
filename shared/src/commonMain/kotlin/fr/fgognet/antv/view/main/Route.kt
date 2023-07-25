package fr.fgognet.antv.view.main

import fr.fgognet.antv.MR
import kotlinx.serialization.Serializable

@Serializable
enum class Route(val value: String) {
    LIVE("live"),
    PLAYLIST("playlist"),
    SEARCH("search"),
    REPLAY("replay"),
    PLAYER("player"),

}

fun findBy(value: String): Route? {
    return Route.values().firstOrNull { it.value == value }
}


@Serializable
data class RouteData(
    val id: Route,
    val arguments: List<String>,
)

val routeNames = mapOf(
    Route.LIVE to MR.strings.menu_live,
    Route.PLAYLIST to MR.strings.menu_playlist,
    Route.SEARCH to MR.strings.menu_search,
    Route.REPLAY to MR.strings.title_replay
)

val routeIcons = mapOf(
    Route.LIVE to MR.images.ic_baseline_live_tv_24,
    Route.PLAYLIST to MR.images.ic_baseline_ondemand_video_24,
    Route.SEARCH to MR.images.ic_baseline_search_24,
)


val allRoutes = mapOf(
    Route.LIVE to RouteData(
        id = Route.LIVE,
        arguments = arrayListOf(),
    ),
    Route.PLAYLIST to RouteData(
        id = Route.PLAYLIST,
        arguments = arrayListOf(),
    ),
    Route.SEARCH to RouteData(
        id = Route.SEARCH,
        arguments = arrayListOf(),
    ),
    Route.REPLAY to RouteData(
        id = Route.REPLAY,
        arguments = arrayListOf(),
    ),
    Route.PLAYER to RouteData(
        id = Route.PLAYER,
        arguments = arrayListOf("title"),
    ),
)
