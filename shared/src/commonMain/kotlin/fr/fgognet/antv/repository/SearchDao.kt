package fr.fgognet.antv.repository

import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.utils.ResourceOrText
import io.github.aakira.napier.Napier

private const val TAG = "ANTV/SearchDao"

data class SearchEntity(
    var label: ResourceOrText,
    val queryParams: HashMap<EventSearchQueryParams, String>
)

private var data: SearchEntity? = null

object SearchDao {
    fun set(entity: SearchEntity) {
        Napier.v("set", tag = TAG)
        data = entity
    }

    fun get(): SearchEntity? {
        Napier.v("get", tag = TAG)
        return data
    }
    
}