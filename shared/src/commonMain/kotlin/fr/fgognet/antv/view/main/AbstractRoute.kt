package fr.fgognet.antv.view.main

import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import fr.fgognet.antv.MR

enum class Route(val value: String) {
    LIVE("live"),
    PLAYLIST("playlist"),
    SEARCH("search"),
    REPLAY("replay"),
    PLAYER("player")
}


data class RouteData(
    val id: Route,
    val nameID: StringResource?,
    val iconName: ImageResource?,
    val arguments: List<String>?,
) {
}


val allRoutes = hashMapOf(
    Route.LIVE to RouteData(
        id = Route.LIVE,
        arguments = arrayListOf(),
        nameID = MR.strings.menu_live,
        iconName = MR.images.ic_baseline_live_tv_24,
    ),
    Route.PLAYLIST to RouteData(
        id = Route.PLAYLIST,
        arguments = arrayListOf(),
        nameID = MR.strings.menu_playlist,
        iconName = MR.images.ic_baseline_ondemand_video_24,
    ),
    Route.SEARCH to RouteData(
        id = Route.SEARCH,
        arguments = arrayListOf(),
        nameID = MR.strings.menu_search,
        iconName = MR.images.ic_baseline_search_24,
    ),
    Route.REPLAY to RouteData(
        id = Route.REPLAY,
        arguments = arrayListOf(),
        nameID = MR.strings.title_replay,
        iconName = null,
    ),
    Route.PLAYER to RouteData(
        id = Route.PLAYER,
        arguments = arrayListOf("title"),
        nameID = null,
        iconName = null,
    ),
)
