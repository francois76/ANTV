package fr.fgognet.antv.view.live

import androidx.lifecycle.ViewModelProvider
import fr.fgognet.antv.R


/**
 * LiveFragment is the main fragment handle by navigation
 */
class LiveFragment : AbstractCardListFragment() {
    override fun initViewModelProvider(): AbstractCardListViewModel {
        return ViewModelProvider(this)[LiveViewModel::class.java]
    }

    override fun getTitle(): String {
        return return resources.getText(R.string.title_live).toString()
    }

    override fun getResource(): Int {
        return R.layout.fragment_live
    }

}