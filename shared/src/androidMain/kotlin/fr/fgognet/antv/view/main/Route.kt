package fr.fgognet.antv.view.main

import android.os.Bundle
import androidx.navigation.*
import dev.icerock.moko.resources.StringResource
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

actual class RouteData(
    val id: String,
    val arguments: List<NamedNavArgument>?,
    val deepLinks: List<NavDeepLink>?,
    val nameID: StringResource?,
    val iconName: String?
)

actual fun convertRouteData(routeId: Routes, abstract: AbstractRouteData): RouteData {
    var arguments: List<NamedNavArgument>? = null
    var deepLinks: List<NavDeepLink>? = null
    if (abstract.argumentNames.isNotEmpty()) {
        arguments = abstract.argumentNames.map {
            navArgument(it) { type = NavType.StringType }
        }
        deepLinks = arrayListOf(navDeepLink {
            uriPattern =
                "antv://${routeId.value}/{${abstract.argumentNames.joinToString(separator = "}/{")}}"
        })
    }
    return RouteData(
        id = routeId.value,
        nameID = abstract.nameID,
        iconName = abstract.iconName,
        arguments = arguments,
        deepLinks = deepLinks
    )
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