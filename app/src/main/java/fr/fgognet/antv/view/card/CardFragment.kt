package fr.fgognet.antv.view.card

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.fgognet.antv.R

private const val TAG = "ANTV/CardFragment"


/**
 * CardFragment fragment that handle each element representing a card on the homepage
 */
class CardFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_card, container, false)
    }


}