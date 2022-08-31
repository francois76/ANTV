package fr.fgognet.antv.jetpackView.replaySearch

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.mapping.Bundle
import io.github.aakira.napier.Napier
import kotlinx.datetime.*

private const val TAG = "ANTV/ReplaySearchViewModel"

class ReplaySearchViewModel : ViewModel() {


    fun makeSearchBundle(currentDate: Long): Bundle {
        val date: LocalDateTime =
            Instant.fromEpochMilliseconds(
                currentDate
            ).toLocalDateTime(TimeZone.currentSystemDefault())
        val bundle = Bundle()

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
        bundle.putString(
            EventSearchQueryParams.Date.toString(),
            "$dateMorning-$dateEvening"
        )
        bundle.putString(
            EventSearchQueryParams.Tag.toString(),
            // resources.getString(R.string.search_description)
            "Recherche personnalisée"
        )
        Napier.d(
            "search Time: $date",
            tag = TAG
        )
        return bundle
    }

}