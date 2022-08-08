package fr.fgognet.antv.view.cardList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import fr.fgognet.antv.R
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.utils.debounce
import fr.fgognet.antv.view.card.CardAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

private const val TAG = "ANTV/AbstractCardListFragment"

/**
 * AbstractCardListFragment is the main fragment handle by navigation
 */
abstract class AbstractCardListFragment : Fragment() {

    private lateinit var model: AbstractCardListViewModel


    abstract fun initViewModelProvider(savedInstanceState: Bundle?): AbstractCardListViewModel
    abstract fun getTitle(): String
    abstract fun getResource(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "onViewCreated")

        super.onViewCreated(view, savedInstanceState)
        view.rootView.findViewById<MaterialToolbar>(R.id.topAppBar).title = getTitle()
        if (savedInstanceState != null) {
            view.findViewById<TextView>(R.id.cardListTitle).text =
                savedInstanceState.getString("title")
        }
        model = initViewModelProvider(savedInstanceState)
        model.loadCardData(savedInstanceState, false)
        if (PlayerService.currentMediaData != null) {
            view.findViewById<MaterialCardView>(R.id.is_playing_card).visibility = View.VISIBLE
            view.findViewById<MaterialCardView>(R.id.is_playing_card).setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.playerFragment, null)
            }
            view.findViewById<TextView>(R.id.is_playing_title).text =
                PlayerService.currentMediaData!!.title
            view.findViewById<ImageView>(R.id.is_playing_thumbnail)
                .setImageBitmap(PlayerService.currentMediaData!!.bitmap)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.editos)
        val cardAdapter = CardAdapter { cardData -> loadRemoteCardData(cardData) }
        recyclerView.adapter = cardAdapter
        model.cardListData.debounce(500L, CoroutineScope(Dispatchers.Main))
            .observe(viewLifecycleOwner) {
                Log.d(TAG, "updating cardList with following: $it")
                cardAdapter.submitList(it.cards as MutableList<CardData>)
                view.findViewById<TextView>(R.id.cardListTitle).text = it.title
            }
        view.rootView.findViewById<MaterialToolbar>(R.id.topAppBar).menu.findItem(R.id.action_reload)
            .setOnMenuItemClickListener {
                model.loadCardData(savedInstanceState, true)
                view.dispatchConfigurationChanged(resources.configuration)
                true
            }
    }

    private fun loadRemoteCardData(cardData: CardData): CardData {
        return model.loadRemoteCardData(cardData)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.v(TAG, "onSaveInstanceState")
        outState.putString(
            "title",
            view?.findViewById<TextView>(R.id.cardListTitle)?.text.toString()
        )
        super.onSaveInstanceState(outState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(getResource(), container, false)
    }

    override fun onDestroy() {
        Log.v(TAG, "onDestroy")
        super.onDestroy()
    }


}