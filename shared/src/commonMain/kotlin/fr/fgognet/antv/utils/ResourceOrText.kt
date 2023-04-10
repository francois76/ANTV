package fr.fgognet.antv.utils

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource

class ResourceOrText(
    var string: String? = null,
    var res: StringResource? = null,
) {
    @Composable
    override fun toString(): String {
        if (string != null) {
            return string!!
        }
        if (res == null) {
            return ""
        }
        return stringResource(res!!)
    }


}
