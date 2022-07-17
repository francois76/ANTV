package fr.fgognet.antv.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import fr.fgognet.antv.Diffusion
import fr.fgognet.antv.R
import fr.fgognet.antv.service.StreamManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    val TAG = "MainFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
                            val button = Button(context)
                            button.text = diffusion.libelle
                            Log.w(TAG, "adding button" + diffusion.libelle + "to fragment")
                            button.setOnClickListener {
                                Navigation.findNavController(it).navigate(R.id.playerFragment)
                            }
                            view.findViewById<LinearLayout>(R.id.editos).addView(button)
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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        return view
    }


}