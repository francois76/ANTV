package fr.fgognet.antv.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import fr.fgognet.antv.Diffusion
import fr.fgognet.antv.R
import fr.fgognet.antv.service.StreamManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * MainFragment is the main fragment handle by navigation
 */
class MainFragment : Fragment() {
    private val TAG = "ANTV/MainFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val editos = StreamManager.getLiveInfos()
                withContext(Dispatchers.Main) {
                    Log.w(TAG, editos.toString())
                    if (editos?.diffusions == null) {
                        val textView = TextView(context)
                        textView.text = editos!!.introduction
                        view.findViewById<LinearLayout>(R.id.editos).addView(textView)
                    } else {
                        for (diffusion: Diffusion in editos.diffusions!!) {
                            val fragTransaction: FragmentTransaction =
                                parentFragmentManager.beginTransaction()
                            Log.w(TAG, "adding card" + diffusion.libelle + "to fragment")
                            val cardFragment =
                                CardFragment.newInstance(
                                    diffusion.libelle ?: "dissusion sans titre",
                                    diffusion.lieu ?: "lieu inconnu",
                                    diffusion.sujet ?: "",
                                    if (diffusion.id_organe != null) "https://videos.assemblee-nationale.fr/live/images/" + diffusion.id_organe + ".jpg" else "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg",
                                    diffusion.video_url ?: ""
                                )
                            fragTransaction.add(R.id.editos, cardFragment, "fragment$cardFragment")
                            fragTransaction.commit()
                        }
                    }
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