package fr.fgognet.antv.service.player

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.util.TypedValue
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media.session.MediaButtonReceiver
import androidx.media3.cast.CastPlayer
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.android.gms.cast.framework.CastContext
import fr.fgognet.antv.R
import fr.fgognet.antv.activity.main.MainActivity
import fr.fgognet.antv.activity.tv.TvActivity


class PlayerService : MediaSessionService() {
    // TAG
    private val TAG = "ANTV/PlayerService"

    // players
    private lateinit var localPlayer: ExoPlayer
    private lateinit var castPlayer: CastPlayer

    // mediaSession
    private lateinit var mediaSession: MediaSession


    @UnstableApi
    override fun onCreate() {
        super.onCreate()
        Log.v(TAG, "onCreate")
        castPlayer = CastPlayer(CastContext.getSharedInstance(applicationContext))
        localPlayer =
            ExoPlayer.Builder(this).build()
        val newPlayer: Player = if (castPlayer.isCastSessionAvailable) {
            castPlayer
        } else {
            localPlayer
        }
        val servicePlayerListener = PlayerServiceListener(this)
        mediaSession =
            MediaSession.Builder(this, newPlayer)
                .setSessionActivity(TaskStackBuilder.create(this).run {
                    addNextIntent(Intent(this@PlayerService, MainActivity::class.java))
                    addNextIntent(Intent(this@PlayerService, TvActivity::class.java))
                    getPendingIntent(
                        0,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }).setCallback(servicePlayerListener)
                .build()
        castPlayer.setSessionAvailabilityListener(servicePlayerListener)
    }


    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        Log.v(TAG, "onGetSession")
        return mediaSession
    }

    override fun onDestroy() {
        Log.v(TAG, "onDestroy")
        mediaSession.player.release()
        mediaSession.release()
        super.onDestroy()
    }


    fun resyncOnLiveError() {
        // Re-initialize player at the current live window default position.
        if (mediaSession.player.isCurrentMediaItemLive) {
            mediaSession.player.seekToDefaultPosition()
            mediaSession.player.prepare()
        }
    }

    fun cast() {
        Log.v(TAG, "cast")
        setCurrentPlayer(castPlayer)
    }

    fun stopCast() {
        Log.v(TAG, "stopCast")
        setCurrentPlayer(localPlayer)
    }


    private fun setCurrentPlayer(currentPlayer: Player) {
        Log.v(TAG, "setCurrentPlayer")
        if (currentPlayer === mediaSession.player) {
            return
        }
        val previousPlayer: Player = mediaSession.player
        // Player state management.
        var playbackPositionMs = C.TIME_UNSET
        var playWhenReady = false
        // Save state from the previous player.
        val playbackState = previousPlayer.playbackState
        if (playbackState != Player.STATE_ENDED) {
            playbackPositionMs = previousPlayer.currentPosition
            playWhenReady = previousPlayer.playWhenReady
        }
        currentPlayer.setMediaItem(currentMediaItem!!, playbackPositionMs)
        previousPlayer.stop()
        previousPlayer.clearMediaItems()

        currentPlayer.playWhenReady = playWhenReady
        currentPlayer.prepare()
        Log.d(TAG, "updating player")
        mediaSession.player = currentPlayer
    }

    fun showMediaplayerNotification(isPlaying: Boolean) {
        Log.v(TAG, "showNotification")
        val channelID = "media_playback_channel"
        val mNotificationManager = NotificationManagerCompat.from(this)
        mNotificationManager.createNotificationChannel(
            NotificationChannelCompat.Builder(
                channelID,
                NotificationManager.IMPORTANCE_LOW
            )
                .setDescription("Media playback controls")
                .setName("Media playback")
                .setShowBadge(false).build()
        )

        val background = TypedValue()
        this.theme?.resolveAttribute(
            android.R.attr.colorBackground,
            background,
            true
        )
        mNotificationManager.notify(
            0,
            NotificationCompat.Builder(this, channelID)
                .setContentTitle(
                    this.mediaSession.player.mediaMetadata.title
                )
                .setContentText(this.mediaSession.player.mediaMetadata.description)
                .setContentIntent(
                    PendingIntent.getActivity(
                        this, 0, Intent(
                            this,
                            MainActivity::class.java
                        ), PendingIntent.FLAG_IMMUTABLE
                    )
                )
                .setSmallIcon(R.mipmap.ic_launcher)
                // .setLargeIcon(PlayerService.currentMediaData?.bitmap)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(background.data)
                .setColorized(true)
                .addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_baseline_skip_previous_24,
                        "restart",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            this,
                            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                        )
                    )
                )
                .addAction(
                    if (isPlaying) NotificationCompat.Action(
                        R.drawable.ic_baseline_pause_24, "pause",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            this,
                            PlaybackStateCompat.ACTION_PAUSE
                        )
                    ) else NotificationCompat.Action(
                        R.drawable.ic_baseline_play_arrow_24, "play",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            this,
                            PlaybackStateCompat.ACTION_PLAY
                        )
                    )
                )
                .addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_baseline_skip_next_24,
                        "next",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            this,
                            PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        )
                    )
                ).build()
        )
    }

    companion object {
        var controller: MediaController? = null

        // waiting for next version of media3
        var currentMediaItem: MediaItem? = null
    }


}