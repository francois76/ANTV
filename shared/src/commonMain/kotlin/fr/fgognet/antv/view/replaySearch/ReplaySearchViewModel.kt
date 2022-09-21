package fr.fgognet.antv.view.replaySearch

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import io.github.aakira.napier.Napier
import kotlinx.datetime.*

private const val TAG = "ANTV/ReplaySearchViewModel"

class ReplaySearchViewModel : ViewModel() {

    fun start() = apply { }


    fun makeSearchBundle(currentDate: Long): Map<EventSearchQueryParams, String> {
        val date: LocalDateTime =
            Instant.fromEpochMilliseconds(
                currentDate
            ).toLocalDateTime(TimeZone.currentSystemDefault())
        val result = hashMapOf<EventSearchQueryParams, String>()

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
        result[EventSearchQueryParams.Date] = "$dateMorning-$dateEvening"
        result[EventSearchQueryParams.Tag] =
            "Recherche personnalisée" // resources.getString(R.string.search_description)
        Napier.d(
            "search Time: $date",
            tag = TAG
        )
        return result
    }

}