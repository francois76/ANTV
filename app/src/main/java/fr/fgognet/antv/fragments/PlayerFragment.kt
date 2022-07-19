package fr.fgognet.antv.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ui.StyledPlayerView
import fr.fgognet.antv.R
import fr.fgognet.antv.viewmodel.VideoViewModel


private const val ARG_URL = "url"
private const val TAG = "PlayerFragment"

/**
 * PlayerFragment the fragment that hosts the player implementation
 */
class PlayerFragment : Fragment() {
    private var url: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(ARG_URL)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val videoView = ViewModelProvider(this)[VideoViewModel::class.java]
        videoView.updateUrl(url!!)
        videoView.player.observe(viewLifecycleOwner) {
            Log.i(TAG, "refreshing player with URL$url")
            this.view?.findViewById<StyledPlayerView>(R.id.video_view)?.player = it
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }


    companion object {

        @JvmStatic
        fun newInstance(url: String) =
            PlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URL, url)
                }
            }
    }
}