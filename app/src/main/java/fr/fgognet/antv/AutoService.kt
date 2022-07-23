package fr.fgognet.antv

import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import androidx.media.MediaBrowserServiceCompat
import java.util.*

class AutoService : MediaBrowserServiceCompat() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(UUID.randomUUID().toString(), null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        

        val mediaItems: MutableList<MediaBrowserCompat.MediaItem> = mutableListOf()


        result.sendResult(mediaItems)
    }


}