package fr.fgognet.antv.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import fr.fgognet.antv.R
import fr.fgognet.antv.service.StreamManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "ANTV/CardFragment"
private const val ARG_TITLE = "title"
private const val ARG_SUBTITLE = "subtitle"
private const val ARG_DESCRIPTION = "description"
private const val ARG_IMAGE = "image"
private const val ARG_LIVE = "live"

/**
 * CardFragment fragment that handle each element representing a card on the homepage
 */
class CardFragment : Fragment() {

    private var title: String? = null
    private var subtitle: String? = null
    private var description: String? = null
    private var image: String? = null
    private var live: String? = null
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            subtitle = it.getString(ARG_SUBTITLE)
            description = it.getString(ARG_DESCRIPTION)
            image = it.getString(ARG_IMAGE)
            live = it.getString(ARG_LIVE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_card, container, false)
        view.findViewById<TextView>(R.id.card_title).text = title
        view.findViewById<TextView>(R.id.card_subtitle).text = subtitle
        view.findViewById<TextView>(R.id.card_description).text = title
        val t = this
        val savedBitmap = savedInstanceState?.getParcelable<Bitmap>("bitmap")
        val imageView = view.findViewById<ImageView>(R.id.card_image_id)
        imageView.contentDescription = title
        if (savedBitmap != null) {
            t.bitmap = savedBitmap
            imageView.setImageBitmap(
                bitmap
            )
        } else {
            // new instance, we need to fetch
            viewLifecycleOwner.lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    t.bitmap = StreamManager.getLiveImage(image!!)
                    Log.w(TAG, "fetched bitmap :$bitmap")
                    withContext(Dispatchers.Main) {
                        imageView.setImageBitmap(
                            bitmap
                        )
                    }
                }
            }
        }

        view.findViewById<TextView>(R.id.go_to_live_btn).setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url", live)
            Navigation.findNavController(it).navigate(R.id.playerFragment, bundle)
        }
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState")
        // outState.putParcelable("bitmap", bitmap)
        super.onSaveInstanceState(outState)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            subtitle: String,
            description: String,
            image: String,
            live: String
        ) =
            CardFragment().apply {
                Log.d(TAG, "new instance of cardFragment")
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_SUBTITLE, subtitle)
                    putString(ARG_SUBTITLE, description)
                    putString(ARG_IMAGE, image)
                    putString(ARG_LIVE, live)
                }
            }
    }
}