package fr.fgognet.antv.view.player

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.dynamite.DynamiteModule
import fr.fgognet.antv.R


private const val ARG_URL = "url"
private const val TAG = "ANTV/PlayerFragment"

/**
 * PlayerFragment the fragment that hosts the player implementation
 */
class PlayerFragment : Fragment() {
    private var url: String? = null

    private var castContext: CastContext? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(ARG_URL)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "onViewCreated")
        // Getting the cast context later than onStart can cause device discovery not to take place.
        try {
            castContext = CastContext.getSharedInstance(this.requireActivity())
        } catch (e: RuntimeException) {
            var cause = e.cause
            while (cause != null) {
                if (cause is DynamiteModule.LoadingException) {
                    return
                }
                cause = cause.cause
            }
            throw e
        }
        super.onViewCreated(view, savedInstanceState)
        val videoView = ViewModelProvider(this)[VideoViewModel::class.java]
        videoView.updateUrl(url!!, castContext!!)
        val playerView = view.findViewById<StyledPlayerView>(R.id.video_view)
        videoView.player.observe(viewLifecycleOwner) {
            Log.i(TAG, "refreshing player with URL$url")
            playerView.player = it

            if (it is CastPlayer) {
                playerView.controllerHideOnTouch = false
                playerView.controllerShowTimeoutMs = 0
                playerView.showController()
                playerView.defaultArtwork = ResourcesCompat.getDrawable(
                    context?.resources!!,
                    R.drawable.ic_baseline_cast_connected_400,
                    context?.theme
                )
            } else { // currentPlayer == localPlayer
                playerView.controllerHideOnTouch = false
                playerView.controllerShowTimeoutMs = StyledPlayerControlView.DEFAULT_SHOW_TIMEOUT_MS
                playerView.defaultArtwork = null
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

}