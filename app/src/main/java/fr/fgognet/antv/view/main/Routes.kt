package fr.fgognet.antv.view.main

import android.os.Bundle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import dev.icerock.moko.resources.StringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.R
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

interface Route {
    val arguments: List<NamedNavArgument>?
    val id: String
    val nameID: StringResource?
    val iconID: Int?
}

object LiveRoute : Route {
    override val id = "live"
    override val nameID = MR.strings.menu_live
    override val iconID = R.drawable.ic_baseline_live_tv_24
    override val arguments: Nothing? = null
}

object PlaylistRoute : Route {
    override val id = "playlist"
    override val nameID = MR.strings.menu_playlist
    override val iconID = R.drawable.ic_baseline_ondemand_video_24
    override val arguments: Nothing? = null
}

object SearchRoute : Route {
    override val id = "search"
    override val nameID = MR.strings.menu_search
    override val iconID = R.drawable.ic_baseline_search_24
    override val arguments: Nothing? = null
}

object ReplayRoute : Route {
    override val id = "replay"
    override val nameID = MR.strings.title_replay
    override val iconID: Nothing? = null
    override val arguments: Nothing? = null
}

object PlayerRoute : Route {
    override val id = "player"
    val deepLinks = listOf(
        navDeepLink { uriPattern = "antv://player/{title}" }
    )
    override val nameID: Nothing? = null
    override val iconID: Nothing? = null
    override val arguments: List<NamedNavArgument> = arrayListOf(navArgument(
        "title"
    )
    { type = NavType.StringType })
}

fun getEncodedArgument(arguments: Bundle?, key: String): String {
    if (arguments == null) {
        return ""
    }
    return URLDecoder.decode(
        arguments.getString(key).toString(),
        StandardCharsets.UTF_8.toString()
    )
}


