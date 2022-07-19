package fr.fgognet.antv.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import fr.fgognet.antv.R
import fr.fgognet.antv.viewmodel.VideoViewModel


private const val ARG_URL = "url"


/**
 * PlayerFragment the fragment that hosts the player implementation
 */
class PlayerFragment : Fragment() {
    private var url: String? = null
    private var videoView: VideoViewModel? = null
    private var player: ExoPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(ARG_URL)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.videoView = ViewModelProvider(this).get()
        if (!this.videoView!!.isPlayerInit) {
            this.player = this.videoView?.initPlayer(url!!)
        } else {
            this.player = this.videoView?.buildPlayer()
        }

    }

    override fun onStart() {
        super.onStart()
        this.view?.findViewById<StyledPlayerView>(R.id.video_view)?.player = this.player
        this.player?.play()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }


    override fun onDestroy() {
        super.onDestroy()
        this.player?.release()
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