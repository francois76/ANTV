package fr.fgognet.antv.view.replaySearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import fr.fgognet.antv.R
import java.util.*


private const val TAG = "ANTV/ReplaySearchFragment"

class ReplaySearchFragment : Fragment() {

    private var currentDate: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_replay_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "onViewCreated")
        view.rootView.findViewById<MaterialToolbar>(R.id.topAppBar).title =
            resources.getText(R.string.title_search)

        view.findViewById<CalendarView>(R.id.calendarView)
            .setOnDateChangeListener { _, year, month, day ->

                val c: Calendar = Calendar.getInstance()
                c.set(year, month, day)
                currentDate = c.timeInMillis
            }
        val time = view.findViewById<CalendarView>(R.id.calendarView).date
        view.findViewById<MaterialButton>(R.id.search_button).setOnClickListener {

            val bundle = Bundle()
            bundle.putLong("time", currentDate)
            Log.d(TAG, "search Time: $time")
            Navigation.findNavController(it).navigate(R.id.replayFragment, bundle)
        }
        super.onViewCreated(view, savedInstanceState)
    }
}