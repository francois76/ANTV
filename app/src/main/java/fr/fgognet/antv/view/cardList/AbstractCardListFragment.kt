package fr.fgognet.antv.view.cardList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import com.google.android.material.appbar.MaterialToolbar
import fr.fgognet.antv.R
import fr.fgognet.antv.utils.debounce
import fr.fgognet.antv.view.card.CardFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

private const val TAG = "ANTV/AbstractCardListFragment"

/**
 * AbstractCardListFragment is the main fragment handle by navigation
 */
abstract class AbstractCardListFragment : Fragment() {

    private lateinit var model: AbstractCardListViewModel

    abstract fun initViewModelProvider(): AbstractCardListViewModel
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
        model = initViewModelProvider()
        model.loadCardData(savedInstanceState, false)
        view.findViewById<Button>(R.id.is_playing_btn).setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.playerFragment, null)
        }

        model.cardListData.debounce(500L, CoroutineScope(Dispatchers.Main))
            .observe(viewLifecycleOwner) {
                Log.d(TAG, "updating cardList with following: $it")
                val fragTransaction: FragmentTransaction =
                    parentFragmentManager.beginTransaction()
                var index = 0
                var fragmentToRemove = parentFragmentManager.findFragmentByTag("cardFragment$index")
                while (fragmentToRemove != null) {
                    Log.d(TAG, "removing fragment cardFragment$index")
                    fragTransaction.remove(fragmentToRemove)
                    index++
                    fragmentToRemove = parentFragmentManager.findFragmentByTag("cardFragment$index")
                }
                fragTransaction.commit()
                view.findViewById<LinearLayout>(R.id.editos).removeAllViews()
                view.findViewById<TextView>(R.id.cardListTitle).text = it.title
                Log.i(TAG, "refreshing cards in view")
                if (it.cards.isNotEmpty()) {
                    val transaction: FragmentTransaction =
                        parentFragmentManager.beginTransaction()
                    for ((indexOfCard, cardData: CardData) in it.cards.withIndex()) {
                        Log.d(TAG, "adding fragment cardFragment$indexOfCard")
                        val card = CardFragment.newInstance(cardData)
                        transaction.add(
                            R.id.editos,
                            card,
                            "cardFragment$indexOfCard"
                        )
                    }
                    transaction.commit()
                }
            }
        view.rootView.findViewById<MaterialToolbar>(R.id.topAppBar).menu.findItem(R.id.action_reload)
            .setOnMenuItemClickListener {
                model.loadCardData(savedInstanceState, true)
                view.dispatchConfigurationChanged(resources.configuration)
                true
            }
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