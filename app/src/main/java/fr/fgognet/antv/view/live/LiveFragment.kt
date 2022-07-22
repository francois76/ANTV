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
import fr.fgognet.antv.R
import fr.fgognet.antv.view.card.CardFragment

private val TAG = "ANTV/LiveFragment"

/**
 * LiveFragment is the main fragment handle by navigation
 */
class LiveFragment : Fragment() {

    lateinit var model: LiveViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(this)[LiveViewModel::class.java]
        model.cards.observe(viewLifecycleOwner) {
            Log.i(TAG, "refreshing editos")
            view.findViewById<LinearLayout>(R.id.editos)?.removeAllViews()
            if (it?.isEmpty() == true) {
                val textView = TextView(context)
                textView.text = "Aucun live aujourd'hui"
                view.findViewById<LinearLayout>(R.id.editos).addView(textView)
            } else {
                val fragTransaction: FragmentTransaction =
                    parentFragmentManager.beginTransaction()
                for (cardData: CardData in it!!) {
                    Log.i(TAG, "adding card" + cardData.title + "to fragment")
                    val card = CardFragment.newInstance(cardData)
                    fragTransaction.add(
                        R.id.editos,
                        card,
                        "fragment$card"
                    )
                }
                fragTransaction.commit()
            }

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.v(TAG, "onCreateView")
        super.onSaveInstanceState(outState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


}