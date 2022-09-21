package fr.fgognet.antv.utils

import android.content.Context
import dev.icerock.moko.resources.desc.ResourceStringDesc

actual fun stringResourceDesc(r: ResourceStringDesc, context: Any): String {
    return r.toString(context = context as Context)
}