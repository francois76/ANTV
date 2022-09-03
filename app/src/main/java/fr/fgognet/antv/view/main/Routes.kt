package fr.fgognet.antv.view.main

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import fr.fgognet.antv.R
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams

interface Route {
    val arguments: List<NamedNavArgument>?
    val id: String
    val nameID: Int?
    val iconID: Int?
}

object LiveRoute : Route {
    override val id = "live"
    override val nameID = R.string.menu_live
    override val iconID = R.drawable.ic_baseline_live_tv_24
    override val arguments: Nothing? = null
}

object PlaylistRoute : Route {
    override val id = "playlist"
    override val nameID = R.string.menu_playlist
    override val iconID = R.drawable.ic_baseline_ondemand_video_24
    override val arguments: Nothing? = null
}

object SearchRoute : Route {
    override val id = "search"
    override val nameID = R.string.menu_search
    override val iconID = R.drawable.ic_baseline_search_24
    override val arguments: Nothing? = null
}

object ReplayRoute : Route {
    override val id = "replay"
    override val nameID = R.string.title_replay
    override val iconID: Nothing? = null
    override val arguments: List<NamedNavArgument> = EventSearchQueryParams.allValues().map {
        navArgument(
            it.toString()
        )
        { type = NavType.StringType }
    }
    val deepLinks = listOf(
        navDeepLink {
            uriPattern =
                "antv://$id?Date={Date}&TypeVideo={TypeVideo}&Commission={Commission}&Tag={Tag}}"
        }
    )
}

object PlayerRoute : Route {
    override val id = "player"
    var url = "url"
    var imageCode = "image_code"
    val deepLinks = listOf(
        navDeepLink { uriPattern = "antv://player/{$url}/{$imageCode}" }
    )
    override val nameID: Nothing? = null
    override val iconID: Nothing? = null
    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(url)
        { type = NavType.StringType },
        navArgument(imageCode)
        { type = NavType.StringType }
    )
}