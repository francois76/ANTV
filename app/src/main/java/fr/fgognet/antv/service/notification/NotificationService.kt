package fr.fgognet.antv.service.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.util.TypedValue
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media.session.MediaButtonReceiver
import fr.fgognet.antv.R
import fr.fgognet.antv.activity.main.MainActivity
import fr.fgognet.antv.service.player.PlayerService

object NotificationService {
    // TAG
    private const val TAG = "ANTV/NotificationService"


    fun showMediaplayerNotification(context: Context, isPlaying: Boolean) {
        Log.v(TAG, "showNotification")
        val channelID = "media_playback_channel"
        val mNotificationManager = NotificationManagerCompat.from(context)
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
        context.theme?.resolveAttribute(
            android.R.attr.colorBackground,
            background,
            true
        )
        mNotificationManager.notify(
            0,
            NotificationCompat.Builder(context, channelID)
                .setContentTitle(
                    PlayerService.currentMediaData?.title
                )
                .setContentText(PlayerService.currentMediaData?.description)
                .setContentIntent(
                    PendingIntent.getActivity(
                        context, 0, Intent(
                            context,
                            MainActivity::class.java
                        ), PendingIntent.FLAG_IMMUTABLE
                    )
                )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(PlayerService.currentMediaData?.bitmap)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(background.data)
                .setColorized(true)
                .setStyle(
                    androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle()
                        .setMediaSession(PlayerService.mediaSessionToken)
                        .setShowActionsInCompactView(0, 1, 2)
                )
                .addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_baseline_skip_previous_24,
                        "restart",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            context,
                            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                        )
                    )
                )
                .addAction(
                    if (isPlaying) NotificationCompat.Action(
                        R.drawable.ic_baseline_pause_24, "pause",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            context,
                            PlaybackStateCompat.ACTION_PAUSE
                        )
                    ) else NotificationCompat.Action(
                        R.drawable.ic_baseline_play_arrow_24, "play",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            context,
                            PlaybackStateCompat.ACTION_PLAY
                        )
                    )
                )
                .addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_baseline_skip_next_24,
                        "next",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            context,
                            PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                        )
                    )
                ).build()
        )
    }
}