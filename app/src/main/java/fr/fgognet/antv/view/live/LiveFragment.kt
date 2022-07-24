package fr.fgognet.antv.view.live

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import fr.fgognet.antv.R
import fr.fgognet.antv.utils.debounce
import fr.fgognet.antv.view.card.CardFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

private const val TAG = "ANTV/LiveFragment"

/**
 * LiveFragment is the main fragment handle by navigation
 */
class LiveFragment : Fragment() {

    private lateinit var model: LiveViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "onViewCreated")


        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(this)[LiveViewModel::class.java]

        model.liveData.debounce(500L, CoroutineScope(Dispatchers.Main))
            .observe(viewLifecycleOwner) {
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
                view.rootView.findViewById<MaterialToolbar>(R.id.topAppBar).title = it.title
                Log.i(TAG, "refreshing editos in view")
                if (it.cards.isEmpty()) {
                    val textView = TextView(context)
                    textView.text = resources.getText(R.string.no_live)
                    view.findViewById<LinearLayout>(R.id.editos).addView(textView)
                } else {
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
                model.loadCardData()
                view.dispatchConfigurationChanged(resources.configuration)
                true
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.v(TAG, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live, container, false)
    }

    override fun onDestroy() {
        Log.v(TAG, "onDestroy")
        super.onDestroy()
    }


}