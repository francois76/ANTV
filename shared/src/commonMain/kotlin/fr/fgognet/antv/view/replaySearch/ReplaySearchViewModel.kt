package fr.fgognet.antv.view.replaySearch

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.MR
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.repository.SearchDao
import fr.fgognet.antv.repository.SearchEntity
import fr.fgognet.antv.utils.ResourceOrText
import io.github.aakira.napier.Napier
import kotlinx.datetime.*

private const val TAG = "ANTV/ReplaySearchViewModel"

class ReplaySearchViewModel : ViewModel() {

    fun start() = apply { }


    fun makeSearchBundle(currentDate: Long) {
        val date: LocalDateTime =
            Instant.fromEpochMilliseconds(
                currentDate
            ).toLocalDateTime(TimeZone.currentSystemDefault())
        val queryParams = hashMapOf<EventSearchQueryParams, String>()

        val dateMorning = LocalDateTime(
            date.year,
            date.month,
            date.dayOfMonth,
            8,
            0
        ).toInstant(TimeZone.currentSystemDefault()).epochSeconds
        val dateEvening = LocalDateTime(
            date.year, date.month, date.dayOfMonth, 22, 0
        ).toInstant(TimeZone.currentSystemDefault()).epochSeconds
        queryParams[EventSearchQueryParams.Date] = "$dateMorning-$dateEvening"
        SearchDao.set(
            SearchEntity(
                label = ResourceOrText(res = MR.strings.search_description),
                queryParams = queryParams
            )
        )

        Napier.d(
            "search Time: $date",
            tag = TAG
        )
    }

}