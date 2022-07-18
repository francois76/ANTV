package fr.fgognet.antv.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import fr.fgognet.antv.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_TITLE = "title"
private const val ARG_SUBTITLE = "subtitle"
private const val ARG_IMAGE = "image"
private const val ARG_LIVE = "live"

/**
 * A simple [Fragment] subclass.
 * Use the [CardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CardFragment : Fragment() {
    private var title: String? = null
    private var subtitle: String? = null
    private var image: String? = null
    private var live: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            subtitle = it.getString(ARG_SUBTITLE)
            image = it.getString(ARG_IMAGE)
            live = it.getString(ARG_LIVE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_card, container, false)
        view.findViewById<TextView>(R.id.card_title).text = title
        view.findViewById<TextView>(R.id.go_to_live_btn).setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url", live)
            Navigation.findNavController(it).navigate(R.id.playerFragment, bundle)
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, subtitle: String, image: String, live: String) =
            CardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_SUBTITLE, subtitle)
                    putString(ARG_IMAGE, image)
                    putString(ARG_LIVE, live)
                }
            }
    }
}