package fr.fgognet.antv.view.replaySearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import fr.fgognet.antv.R

private const val TAG = "ANTV/ReplaySearchFragment"

class ReplaySearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        val view = inflater.inflate(R.layout.fragment_replay_search, container, false)

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "onViewCreated")
        view.rootView.findViewById<MaterialToolbar>(R.id.topAppBar).title =
            resources.getText(R.string.title_search)
        super.onViewCreated(view, savedInstanceState)
    }
}