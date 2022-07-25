package fr.fgognet.antv.view.cardList.replay

import androidx.lifecycle.ViewModelProvider
import fr.fgognet.antv.R
import fr.fgognet.antv.view.live.AbstractCardListFragment
import fr.fgognet.antv.view.live.AbstractCardListViewModel

class ReplayFragment : AbstractCardListFragment() {
    override fun initViewModelProvider(): AbstractCardListViewModel {
        return ViewModelProvider(this)[ReplayViewModel::class.java]
    }

    override fun getTitle(): String {
        return resources.getText(R.string.title_replay).toString()
    }

    override fun getResource(): Int {
        return R.layout.fragment_replay
    }
}