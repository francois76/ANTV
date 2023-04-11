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


abstract class RouteDataRaw constructor(
    var id: String,
    var nameID: StringResource?,
    var iconName: String?,
    var arguments: List<NamedNavArgument>?,
    var deepLinks: List<NavDeepLink>?
)

expect class RouteData(
    id: String,
    nameID: StringResource?,
    iconName: String?,
    argumentsRaw: List<String>?,
) : RouteDataRaw

expect class NamedNavArgument
expect class NavDeepLink

val allRoutes = hashMapOf(
    Routes.LIVE to RouteData(
        id = Routes.LIVE.value,
        argumentsRaw = arrayListOf(),
        nameID = MR.strings.menu_live,
        iconName = "ic_baseline_live_tv_24",
    ),
    Routes.PLAYLIST to RouteData(
        id = Routes.PLAYLIST.value,
        argumentsRaw = arrayListOf(),
        nameID = MR.strings.menu_playlist,
        iconName = "ic_baseline_ondemand_video_24",
    ),
    Routes.SEARCH to RouteData(
        id = Routes.SEARCH.value,
        argumentsRaw = arrayListOf(),
        nameID = MR.strings.menu_search,
        iconName = "ic_baseline_search_24",
    ),
    Routes.REPLAY to RouteData(
        id = Routes.REPLAY.value,
        argumentsRaw = arrayListOf(),
        nameID = MR.strings.title_replay,
        iconName = null,
    ),
    Routes.PLAYER to RouteData(
        id = Routes.PLAYER.value,
        argumentsRaw = arrayListOf("title"),
        nameID = null,
        iconName = null,
    ),
)
