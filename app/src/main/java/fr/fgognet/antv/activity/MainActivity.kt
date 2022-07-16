package fr.fgognet.antv.activity

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.MimeTypes
import fr.fgognet.antv.Diffusion
import fr.fgognet.antv.R
import fr.fgognet.antv.service.StreamManager


/**
 * Loads [MainFragment].
 */
class MainActivity : FragmentActivity() {
    var player: ExoPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val editos = StreamManager.getLiveInfos()
        setContentView(R.layout.activity_main)
        val layout = findViewById<LinearLayout>(R.id.edito_elements)
        if (editos?.element == null) {
            var textView = TextView(this)
            textView.text = editos!!.introduction
            layout.addView(textView)
        } else {
            for (diffusion: Diffusion in editos.element!!) {
                var textView = TextView(this)
                textView.text = diffusion.sujet
                layout.addView(textView)
            }
        }

    }

    override fun onStart() {
        super.onStart()

        this.player = ExoPlayer.Builder(this).build()
        val mediaItem: MediaItem =
            MediaItem.Builder()
                .setUri(StreamManager.getOriginalStreamUrl())
                .setMimeType(MimeTypes.APPLICATION_M3U8).build()
        this.player!!.setMediaItem(mediaItem)
        this.player!!.prepare()
        this.player!!.play()
        val view = findViewById<StyledPlayerView>(R.id.video_view)
        view.player = this.player

    }

    override fun onDestroy() {
        super.onDestroy()
        this.player!!.release()
    }

}