package fr.fgognet.antv.view.main

import android.os.Bundle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


fun getEncodedArgument(arguments: Bundle?, key: String): String {
    if (arguments == null) {
        return ""
    }
    return URLDecoder.decode(
        arguments.getString(key).toString(),
        StandardCharsets.UTF_8.toString()
    )
}


actual class NamedNavArgument(value: androidx.navigation.NamedNavArgument)
actual class NavDeepLink(value: androidx.navigation.NavDeepLink)


actual class RouteData actual constructor(
    id: String,
    nameID: StringResource?,
    iconName: ImageResource?,
    argumentsRaw: List<String>?
) : RouteDataRaw(id = id, nameID = nameID, iconName = iconName, arguments = argumentsRaw?.map {
    NamedNavArgument(navArgument(it) { type = NavType.StringType })
}, deepLinks = arrayListOf(NavDeepLink(navDeepLink {
    uriPattern =
        "antv://${id}/{${argumentsRaw?.joinToString(separator = "}/{")}}"
})))