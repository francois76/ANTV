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
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.fgognet.antv.R
import fr.fgognet.antv.external.Images.ImageRepository
import fr.fgognet.antv.service.MediaData
import fr.fgognet.antv.service.PlayerService


const val ARG_URL = "url"
const val ARG_TITLE = "title"
const val ARG_DESCRIPTION = "description"
const val ARG_IMAGE_CODE = "imageCode"
private const val TAG = "ANTV/PlayerFragment"

/**
 * PlayerFragment the fragment that hosts the player implementation
 */
class PlayerFragment : Fragment() {
    private var mediaData: MediaData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        requireActivity().window.decorView.keepScreenOn = true
        arguments?.let {
            mediaData = MediaData(
                it.getString(ARG_URL) ?: "",
                it.getString(ARG_TITLE),
                it.getString(ARG_DESCRIPTION),
                ImageRepository.imageCodeToBitmap[it.getString(ARG_IMAGE_CODE) ?: ""],
            )
        }
    }

    override fun onDestroy() {
        Log.v(TAG, "onDestroy")
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        requireActivity().window.decorView.keepScreenOn = false
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "onViewCreated")

        super.onViewCreated(view, savedInstanceState)
        val videoView = ViewModelProvider(this)[VideoViewModel::class.java]
        this.context?.let { PlayerService.updateCurrentMedia(mediaData!!) }
        val playerView = view.findViewById<StyledPlayerView>(R.id.video_view)
        val topBar = view.rootView.findViewById<MaterialToolbar>(R.id.topAppBar)
        val bottom = view.rootView.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        topBar.visibility = View.GONE
        bottom.visibility = View.GONE
        playerView.setControllerVisibilityListener(StyledPlayerView.ControllerVisibilityListener { visibility: Int ->
            Log.v(TAG, "Player controler visibility Changed: $visibility")
            when (visibility) {
                View.VISIBLE -> {
                    topBar.visibility = View.VISIBLE
                    bottom.visibility = View.VISIBLE
                    activity?.window?.decorView?.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_VISIBLE
                }
                View.GONE -> {
                    topBar.visibility = View.GONE
                    bottom.visibility = View.GONE
                    activity?.window?.decorView?.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                }
            }
        })
        videoView.player.observe(viewLifecycleOwner) {
            Log.i(TAG, "refreshing player with URL ${mediaData?.url}")
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