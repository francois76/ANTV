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
import fr.fgognet.antv.Diffusion
import fr.fgognet.antv.R
import fr.fgognet.antv.service.StreamManager
import fr.fgognet.antv.view.card.CardFragment

private val TAG = "ANTV/MainFragment"

/**
 * LiveFragment is the main fragment handle by navigation
 */
class LiveFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        val editosView = ViewModelProvider(this)[EditoViewModel::class.java]
        editosView.editos.observe(viewLifecycleOwner) {
            view.findViewById<LinearLayout>(R.id.editos).removeAllViews()
            Log.i(TAG, "refreshing editos")
            if (it?.diffusions == null) {
                val textView = TextView(context)
                textView.text = it!!.introduction
                view.findViewById<LinearLayout>(R.id.editos).addView(textView)
            } else {
                for (diffusion: Diffusion in it.diffusions!!) {
                    val fragTransaction: FragmentTransaction =
                        parentFragmentManager.beginTransaction()
                    Log.i(TAG, "adding card" + diffusion.libelle + "to fragment")
                    val cardFragment =
                        CardFragment.newInstance(
                            diffusion.libelle ?: "discussion sans titre",
                            diffusion.lieu ?: "lieu inconnu",
                            diffusion.sujet?.replace("<br>", "\n") ?: "",
                            if (diffusion.id_organe != null) "https://videos.assemblee-nationale.fr/live/images/" + diffusion.id_organe + ".jpg" else "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                            diffusion.video_url ?: "",
                            StreamManager.getLiveButtonLabel(diffusion)
                        )
                    fragTransaction.add(R.id.editos, cardFragment, "fragment$cardFragment")
                    fragTransaction.commit()

                }
            }

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


}