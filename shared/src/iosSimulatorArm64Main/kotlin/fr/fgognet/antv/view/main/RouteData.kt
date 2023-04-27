package fr.fgognet.antv.view.main

import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource

actual class RouteData actual constructor(
    id: String,
    nameID: StringResource?,
    iconName: ImageResource?,
    argumentsRaw: List<String>?,

    ) : RouteDataRaw(
    id = id,
    nameID = nameID,
    iconName = iconName,
    arguments = arrayListOf(),
    deepLinks = arrayListOf()
)

actual class NamedNavArgument
actual class NavDeepLink