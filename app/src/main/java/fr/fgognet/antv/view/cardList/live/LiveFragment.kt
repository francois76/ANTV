package fr.fgognet.antv.view.cardList.live

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import fr.fgognet.antv.R
import fr.fgognet.antv.view.cardList.AbstractCardListFragment
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel


/**
 * LiveFragment is the main fragment handle by navigation
 */
class LiveFragment : AbstractCardListFragment() {
    override fun initViewModelProvider(savedInstanceState: Bundle?): AbstractCardListViewModel {
        return ViewModelProvider(this)[LiveViewModel::class.java]
    }

    override fun getTitle(): String {
        return resources.getText(R.string.title_live).toString()
    }

    override fun getResource(): Int {
        return R.layout.fragment_live
    }

}