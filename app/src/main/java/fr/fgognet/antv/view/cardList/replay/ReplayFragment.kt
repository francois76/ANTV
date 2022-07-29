package fr.fgognet.antv.view.cardList.replay

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import fr.fgognet.antv.R
import fr.fgognet.antv.view.live.AbstractCardListFragment
import fr.fgognet.antv.view.live.AbstractCardListViewModel

private const val TAG = "ANTV/ReplayFragment"

class ReplayFragment : AbstractCardListFragment() {

    private var time: Long = 0

    override fun initViewModelProvider(): AbstractCardListViewModel {
        return ViewModelProvider(this)[ReplayViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.time = it.getLong("time")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "onViewCreated: $savedInstanceState")
        var enrichedBundle = savedInstanceState
        if (enrichedBundle == null) {
            enrichedBundle = Bundle()
        }
        enrichedBundle.putLong("time", time)
        super.onViewCreated(view, enrichedBundle)


    }

    override fun getTitle(): String {
        return resources.getText(R.string.title_replay).toString()
    }

    override fun getResource(): Int {
        return R.layout.fragment_replay
    }
}