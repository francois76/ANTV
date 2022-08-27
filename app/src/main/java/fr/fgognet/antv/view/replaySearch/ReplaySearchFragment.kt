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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import fr.fgognet.antv.R
import fr.fgognet.antv.view.buildColors


private const val TAG = "ANTV/ReplaySearchFragment"

class ReplaySearchFragment : Fragment() {

    private var currentDate: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            ReplaySearchScreen(
                model = replaySearchViewModel()
            )
        }
    }

    @Composable
    fun ReplaySearchScreen(
        model: replaySearchViewModel?
    ) {
        MaterialTheme(colorScheme = buildColors(context = LocalContext.current)) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dp(20F))
            ) {
                lateinit var calendarView: CalendarView
                AndroidView(
                    {
                        calendarView = CalendarView(it)
                        calendarView
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    update = { views ->
                        views.setOnDateChangeListener { _, year, month, day ->
                            val c: Calendar = Calendar.getInstance()
                            c.set(year, month, day)
                            currentDate = c.timeInMillis
                        }
                    }
                )
                Button(
                    onClick = {
                        findNavController().navigate(
                            R.id.replayFragment,
                            model?.makeSearchBundle(currentDate)
                        )
                    },
                    content = {
                        Text(text = stringResource(id = R.string.buttom_search))
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun ReplaySearchScreenPreview(
    ) {
        ReplaySearchScreen(model = null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "onViewCreated")
        view.rootView.findViewById<MaterialToolbar>(R.id.topAppBar).title =
            resources.getText(R.string.title_search)
        super.onViewCreated(view, savedInstanceState)
    }
}