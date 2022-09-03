package fr.fgognet.antv.utils

import android.os.Bundle
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams

fun convertBundleToMap(b: Bundle): HashMap<EventSearchQueryParams, String> {
    val result = hashMapOf<EventSearchQueryParams, String>()
    EventSearchQueryParams.allValues().forEach {
        if (b.containsKey(it.toString())) {
            result[it] =
                b.getString(it.toString()).toString()
        }
    }
    return result
}