package fr.fgognet.antv.view.cardList.playlist

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import fr.fgognet.antv.R
import fr.fgognet.antv.view.cardList.AbstractCardListFragment
import fr.fgognet.antv.view.cardList.AbstractCardListViewModel

private const val TAG = "ANTV/PlaylistFragment"

class PlaylistFragment : AbstractCardListFragment() {


    override fun initViewModelProvider(savedInstanceState: Bundle?): AbstractCardListViewModel {
        return ViewModelProvider(this)[PlaylistViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun getTitle(): String {
        return resources.getText(R.string.title_replay).toString()
    }

    override fun getResource(): Int {
        return R.layout.fragment_playlist
    }
}