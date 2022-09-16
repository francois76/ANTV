package fr.fgognet.antv.utils

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.ResourceStringDesc
import dev.icerock.moko.resources.desc.desc

class ResourceOrText(
    var string: String? = null,
    var stringResource: StringResource? = null,
) {
    fun toString(context: Any): String {
        if (string != null) {
            return string!!
        }
        if (stringResource == null) {
            return ""
        }
        return stringResourceDesc(stringResource!!.desc(), context)

    }


}

expect fun stringResourceDesc(r: ResourceStringDesc, context: Any): String