package fr.fgognet.antv.view.main

import android.os.Bundle
import androidx.navigation.*
import dev.icerock.moko.resources.StringResource
import fr.fgognet.antv.MR
import fr.fgognet.antv.R
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import java.net.URLDecoder
import java.net.URLEncoder
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
    override val arguments: List<NamedNavArgument> =
        getArguments(EventSearchQueryParams.allValues())
    val deepLinks = getNavDeepLinks(id, EventSearchQueryParams.allValues())
}

object PlayerRoute : Route {
    override val id = "player"
    val argumentNames = arrayListOf("url", "image_code", "title", "description")
    val deepLinks = getNavDeepLinks(id, argumentNames)
    override val nameID: Nothing? = null
    override val iconID: Nothing? = null
    override val arguments: List<NamedNavArgument> = getArguments(argumentNames)
}

fun getNavDeepLinks(id: String, argumentNames: List<Any>): List<NavDeepLink> {
    return listOf(
        navDeepLink { uriPattern = "antv://${getRoute(id, argumentNames)}" }
    )
}

fun getRoute(id: String, argumentNames: List<Any>): String {
    return "$id/${argumentNamesToString(argumentNames)}"
}

fun callRouteWithArguments(id: String, arguments: Map<String, String>): String {
    return "$id/${
        arguments.map {
            "${it.key}=${
                URLEncoder.encode(
                    it.value,
                    StandardCharsets.UTF_8.toString()
                )
            }"
        }.sorted().joinToString("/")
    }"
}

fun argumentNamesToString(argumentNames: List<Any>): String {
    return argumentNames.map { it.toString() }.sorted().joinToString("/") { "$it={$it}" }
}

fun getArguments(argumentNames: List<Any>): List<NamedNavArgument> {
    return argumentNames.map {
        navArgument(
            it.toString()
        )
        { type = NavType.StringType }
    }
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