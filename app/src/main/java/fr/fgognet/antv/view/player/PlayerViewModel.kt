package fr.fgognet.antv.view.player

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.util.TypedValue
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.media.session.MediaButtonReceiver
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.setValue
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import fr.fgognet.antv.R
import fr.fgognet.antv.activity.main.MainActivity
import fr.fgognet.antv.repository.VideoDao
import fr.fgognet.antv.service.player.PlayerListener
import fr.fgognet.antv.service.player.PlayerService


private const val TAG = "ANTV/PlayerViewModel"

data class PlayerData(
    val player: Player?,
    val url: String,
    val imageCode: String,
    val title: String,
    val description: String
)

@UnstableApi
@SuppressLint("StaticFieldLeak")
class PlayerViewModel : ViewModel(),
    DefaultLifecycleObserver, PlayerListener {


    fun start(context: Context) = apply { initialize(context) }

    private lateinit var _context: Context
    private val _playerdata: MutableLiveData<PlayerData> =
        MutableLiveData(
            PlayerData(
                url = "",
                imageCode = "",
                title = "",
                description = "",
                player = null
            )
        )
    val playerData: LiveData<PlayerData> get() = _playerdata
    private var listenerKey: Int = 0

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private val controller: MediaController?
        get() = if (controllerFuture.isDone) controllerFuture.get() else null


    private fun initialize(context: Context) {
        Log.v(TAG, "initialize")
        this._context = context
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        Log.d(TAG, "registered $listenerKey")

        controllerFuture =
            MediaController.Builder(
                this._context,
                SessionToken(
                    this._context,
                    ComponentName(this._context, PlayerService::class.java)
                )
            )
                .buildAsync()
        controllerFuture.addListener({

            this._playerdata.setValue(
                PlayerData(
                    player = this.controller,
                    imageCode = this.playerData.value.imageCode,
                    title = this.playerData.value.title,
                    description = this.playerData.value.description,
                    url = this._playerdata.value.url
                ), async = true
            )

            this.controller?.addListener(
                this
            )
        }, MoreExecutors.directExecutor())
    }


    override fun onCleared() {
        Log.v(TAG, "onCleared")
        super.onCleared()
        Log.d(TAG, "de-registered $listenerKey")
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }


    override fun onIsPlayingChanged(isPlaying: Boolean) {
        showMediaplayerNotification(
            this._context,
            isPlaying
        )
    }

    fun updateCurrentMedia() {
        controller?.setMediaItem(
            MediaItem.Builder()
                .setUri(this.playerData.value.url)
                .setMediaMetadata(
                    MediaMetadata.Builder().setTitle(this.playerData.value.title)
                        .setDescription(this.playerData.value.description)
                        .build()
                )
                .setMimeType(MimeTypes.APPLICATION_M3U8).build()
        )
        controller?.prepare()
        controller?.play()
    }

    fun retrievePlayerEntity(title: String) {
        val entity = VideoDao.get(title)
        if (entity != null) {
            val updatedData = PlayerData(
                url = entity.url,
                imageCode = entity.imageCode,
                title = entity.title,
                description = entity.description,
                player = playerData.value.player
            )
            this._playerdata.value = updatedData
        }
    }

    private fun showMediaplayerNotification(context: Context, isPlaying: Boolean) {
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
                    controller?.currentMediaItem?.mediaMetadata?.title
                )
                .setContentText(controller?.currentMediaItem?.mediaMetadata?.description)
                .setContentIntent(
                    PendingIntent.getActivity(
                        context, 0, Intent(
                            context,
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
