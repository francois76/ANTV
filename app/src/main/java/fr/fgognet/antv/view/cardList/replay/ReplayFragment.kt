package fr.fgognet.antv.view.cardList.replay

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import fr.fgognet.antv.R
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.view.cardList.AbstractCardListFragment
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel

private const val TAG = "ANTV/ReplayFragment"

class ReplayFragment : AbstractCardListFragment() {

    lateinit var replayViewModel: ReplayViewModel


    override fun initViewModelProvider(savedInstanceState: Bundle?): AbstractCardListViewModel {
        replayViewModel = ViewModelProvider(this)[ReplayViewModel::class.java]
        arguments?.let { bundle ->
            EventSearchQueryParams.values().forEach { queryParam ->
                bundle.getString(queryParam.toString()).let { value ->
                    if (value != null) {
                        replayViewModel.searchQueryFields.put(
                            queryParam, "$value",
                        )
                    }
                }
            }
        }
        return replayViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }


    override fun getTitle(): String {
        return resources.getText(R.string.title_replay).toString()
    }

    override fun getResource(): Int {
        return R.layout.fragment_replay
    }
}