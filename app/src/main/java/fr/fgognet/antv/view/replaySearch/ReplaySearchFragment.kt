package fr.fgognet.antv.view.replaySearch

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.composethemeadapter.MdcTheme
import fr.fgognet.antv.R
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset


private const val TAG = "ANTV/ReplaySearchFragment"

class ReplaySearchFragment : Fragment() {

    private var currentDate: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            // Create a Compose MaterialTheme inheriting the existing colors, typography
            // and shapes of the current View system's theme
            MdcTheme {
                ReplaySearchScreen()
            }
        }
    }

    @Preview
    @Composable
    fun ReplaySearchScreen(
    ) {
        Column(Modifier.fillMaxWidth()) {
            lateinit var calendarView: CalendarView
            AndroidView(
                {
                    calendarView = CalendarView(it)
                    calendarView
                },
                modifier = Modifier.wrapContentWidth(),
                update = { views ->
                    views.setOnDateChangeListener { _, year, month, day ->
                        val c: Calendar = Calendar.getInstance()
                        c.set(year, month, day)
                        currentDate = c.timeInMillis
                    }
                }
            )
            Button(onClick = {
                val date: LocalDateTime =
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(currentDate),
                        ZoneOffset.systemDefault()
                    )
                val bundle = Bundle()
                val dateMorning =
                    LocalDateTime.of(date.year, date.month, date.dayOfMonth, 8, 0).toEpochSecond(
                        ZoneOffset.UTC
                    )
                val dateEvening =
                    LocalDateTime.of(date.year, date.month, date.dayOfMonth, 22, 0).toEpochSecond(
                        ZoneOffset.UTC
                    )
                bundle.putString(
                    EventSearchQueryParams.Date.toString(),
                    "$dateMorning-$dateEvening"
                )
                bundle.putString(
                    EventSearchQueryParams.Tag.toString(),
                    resources.getString(R.string.search_description)
                )
                Log.d(TAG, "search Time: ${calendarView.date}")
                findNavController().navigate(R.id.replayFragment, bundle)
            }, content = {
                Text(text = resources.getString(R.string.buttom_search))
            }
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "onViewCreated")
        view.rootView.findViewById<MaterialToolbar>(R.id.topAppBar).title =
            resources.getText(R.string.title_search)
        super.onViewCreated(view, savedInstanceState)
    }
}