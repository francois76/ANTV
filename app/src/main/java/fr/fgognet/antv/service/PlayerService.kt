package fr.fgognet.antv.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.util.TypedValue
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media.session.MediaButtonReceiver
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.gms.cast.framework.CastContext
import fr.fgognet.antv.R
import fr.fgognet.antv.activity.MainActivity

data class MediaData(
    val url: String,
    val title: String?,
    val description: String?,
    val bitmap: Bitmap?
)

object PlayerService {
    // TAG
    private const val TAG = "ANTV/PlayerService"

    // players
    private lateinit var localPlayer: Player
    private lateinit var castPlayer: CastPlayer
    private lateinit var player: Player

    // context
    private lateinit var applicationContext: Context

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private var currentMediaData: MediaData? = null
    private var currentMediaItem: MediaItem? = null


    private const val CHANNEL_ID = "media_playback_channel"
    private var mStateBuilder: PlaybackStateCompat.Builder =
        PlaybackStateCompat.Builder().setActions(
            PlaybackStateCompat.ACTION_PLAY or
                    PlaybackStateCompat.ACTION_PAUSE or
                    PlaybackStateCompat.ACTION_PLAY_PAUSE or
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
        )

    class ListenersClass : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            Log.v(TAG, "onIsPlayingChanged")
            super.onIsPlayingChanged(isPlaying)
            if (isPlaying) {
                mStateBuilder.setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    player.currentPosition, 1f
                )
                Log.d(TAG, "PLAYING")
            } else {
                mStateBuilder.setState(
                    PlaybackStateCompat.STATE_PAUSED,
                    player.currentPosition, 1f
                )
                Log.d(TAG, "PAUSED")
            }
            mediaSession.setPlaybackState(mStateBuilder.build())
            showNotification(mStateBuilder.build())
        }
    }

    private val listeners: ListenersClass = ListenersClass()

    fun updateCurrentMedia(mediaData: MediaData) {
        Log.v(TAG, "updateUrl")
        if (mediaData.url == currentMediaData?.url) {
            return
        }
        this.currentMediaData = mediaData
        this.currentMediaItem =
            MediaItem.Builder()
                .setUri(mediaData.url)
                .setMimeType(MimeTypes.APPLICATION_M3U8).build()
        player.setMediaItem(this.currentMediaItem!!)
        player.prepare()
        player.play()
    }

    fun getPlayer(castContext: CastContext, applicationContext: Context): Player {
        Log.v(TAG, "getPlayer")
        if (!this::player.isInitialized) {
            return setUpPlayer(castContext, applicationContext)
        }
        return this.player
    }

    private fun setUpPlayer(castContext: CastContext, applicationContext: Context): Player {
        Log.v(TAG, "setUpPlayer")
        this.applicationContext = applicationContext
        castPlayer = CastPlayer(castContext)
        localPlayer =
            ExoPlayer.Builder(applicationContext).build()
        val newPlayer: Player = if (castPlayer.isCastSessionAvailable) {
            castPlayer
        } else {
            localPlayer
        }
        this.player = newPlayer
        this.mediaSession = initMediaSession()
        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(newPlayer)
        player.addListener(listeners)

        return player
    }

    fun cast() {
        Log.v(TAG, "cast")
        setCurrentPlayer(castPlayer)
    }

    fun stopCast() {
        Log.v(TAG, "stopCast")
        setCurrentPlayer(localPlayer)
    }

    fun <T> registerCastListener(listener: T) where T : SessionAvailabilityListener, T : Player.Listener {
        Log.v(TAG, "registerCastListener")
        castPlayer.addListener(listener)
        castPlayer.setSessionAvailabilityListener(listener)
    }


    private fun initMediaSession(): MediaSessionCompat {
        val mediaSession = MediaSessionCompat(applicationContext, TAG)
        mediaSession.setMediaButtonReceiver(null)
        mediaSession.setPlaybackState(mStateBuilder.build())
        mediaSession.isActive = true
        return mediaSession
    }

    private fun showNotification(state: PlaybackStateCompat) {
        Log.v(TAG, "showNotification")
        val mNotificationManager = NotificationManagerCompat.from(applicationContext)
        mNotificationManager.createNotificationChannel(
            NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManager.IMPORTANCE_LOW)
                .setDescription("Media playback controls")
                .setName("Media playback")
                .setShowBadge(false).build()
        )

        val background = TypedValue()
        applicationContext.theme?.resolveAttribute(
            android.R.attr.colorBackground,
            background,
            true
        )
        mNotificationManager.notify(
            0,
            NotificationCompat.Builder(this.applicationContext, CHANNEL_ID).setContentTitle(
                currentMediaData?.title
            )
                .setContentText(currentMediaData?.description)
                .setContentIntent(
                    PendingIntent.getActivity(
                        applicationContext, 0, Intent(
                            applicationContext,
                            MainActivity::class.java
                        ), PendingIntent.FLAG_IMMUTABLE
                    )
                )
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(currentMediaData?.bitmap)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(background.data)
                .setColorized(true)
                .setStyle(
                    androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle()
                        .setMediaSession(mediaSession.sessionToken)
                        .setShowActionsInCompactView(0, 1, 2)
                )
                .addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_baseline_skip_previous_24,
                        "restart",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            applicationContext,
                            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                        )
                    )
                )
                .addAction(
                    if (state.state == PlaybackStateCompat.STATE_PLAYING) NotificationCompat.Action(
                        R.drawable.ic_baseline_pause_24, "pause",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            applicationContext,
                            PlaybackStateCompat.ACTION_PAUSE
                        )
                    ) else NotificationCompat.Action(
                        R.drawable.ic_baseline_play_arrow_24, "play",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            applicationContext,
                            PlaybackStateCompat.ACTION_PLAY
                        )
                    )
                )
                .addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_baseline_skip_next_24,
                        "next",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            applicationContext,
                            PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        )
                    )
                ).build()
        )
    }

    private fun setCurrentPlayer(currentPlayer: Player) {
        Log.v(TAG, "setCurrentPlayer")
        if (currentPlayer === this.player) {
            return
        }
        val previousPlayer: Player = this.player
        // Player state management.
        var playbackPositionMs = C.TIME_UNSET
        var playWhenReady = false
        // Save state from the previous player.
        val playbackState = previousPlayer.playbackState
        if (playbackState != Player.STATE_ENDED) {
            playbackPositionMs = previousPlayer.currentPosition
            playWhenReady = previousPlayer.playWhenReady
        }
        previousPlayer.stop()
        previousPlayer.clearMediaItems()
        previousPlayer.removeListener(listeners)
        currentPlayer.addListener(listeners)
        currentPlayer.setMediaItem(this.currentMediaItem!!, playbackPositionMs)
        currentPlayer.playWhenReady = playWhenReady
        currentPlayer.prepare()
        mediaSessionConnector.setPlayer(currentPlayer)
        mediaSession.isActive = true
        this.player = currentPlayer
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients
     */
    class MediaReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            MediaButtonReceiver.handleIntent(mediaSession, intent)
        }
    }

}