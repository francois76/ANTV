package fr.fgognet.antv.service.player

import android.util.Log
import androidx.media3.cast.SessionAvailabilityListener
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

private const val TAG = "ANTV/MediaSessionServiceListener"

@UnstableApi
class MediaSessionServiceListener(private val service: MediaSessionServiceImpl) :
    SessionAvailabilityListener,
    MediaSession.Callback {

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<List<MediaItem>> {
        Log.v(TAG, "onAddMediaItems")
        val updatedMediaItems: List<MediaItem> = mediaItems.map { mediaItem ->
            MediaItem.Builder()
                .setUri(mediaItem.mediaId)
                .setMediaId(mediaItem.mediaId)
                .setMediaMetadata(
                    MediaMetadata.Builder().setTitle(mediaItem.mediaMetadata.title)
                        .setDescription(mediaItem.mediaMetadata.description)
                        .setArtworkUri(mediaItem.mediaMetadata.artworkUri)
                        .build()
                )
                .setMimeType(MimeTypes.APPLICATION_M3U8).build()
        }
        if (updatedMediaItems[0].mediaId == mediaSession.player.currentMediaItem?.mediaId) {
            return super.onAddMediaItems(mediaSession, controller, mediaItems)
        }
        MediaSessionServiceImpl.currentMediaItem = updatedMediaItems[0]
        return Futures.immediateFuture(updatedMediaItems)
    }


    override fun onCastSessionAvailable() {
        Log.v(TAG, "onCastSessionAvailable")
        service.cast()
    }


    override fun onCastSessionUnavailable() {
        Log.v(TAG, "onCastSessionUnavailable")
        service.stopCast()
    }


}