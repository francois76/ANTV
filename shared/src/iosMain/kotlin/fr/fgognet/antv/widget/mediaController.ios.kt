package fr.fgognet.antv.widget

import kotlinx.cinterop.CValue
import platform.AVFAudio.*
import platform.AVFoundation.*
import platform.CoreMedia.*
import platform.Foundation.*
import platform.darwin.Float64
import platform.darwin.NSEC_PER_SEC

// inspired by https://github.com/ayodelekehinde/Kicks/blob/master/shared/src/iosMain/kotlin/io/github/kicks/audioplayer/AudioPlayer.kt
actual class MediaController(private val iosPlayer: AVPlayer) {
    private val playerItems = mutableListOf<AVPlayerItem>()

    private var currentItemIndex = -1
    private lateinit var timeObserver: Any

    private val observer: (CValue<CMTime>) -> Unit = { time: CValue<CMTime> ->
        // playerState.isBuffering = iosPlayer.currentItem?.isPlaybackLikelyToKeepUp() != true
        val rawTime: Float64 = CMTimeGetSeconds(time)
        if (iosPlayer.currentItem != null) {
            val cmTime = CMTimeGetSeconds(iosPlayer.currentItem!!.duration)
            // playerState.duration =   if (cmTime.isNaN()) 0 else cmTime.toDuration(DurationUnit.SECONDS).inWholeSeconds
        }
    }

    init {
        setUpAudioSession()
        // playerState.isPlaying = iosPlayer.timeControlStatus == AVPlayerTimeControlStatusPlaying
    }


    actual fun pause() {
        iosPlayer.pause()
    }

    actual fun play() {
        iosPlayer.play()
    }

    actual fun seekTo(toLong: Long) {
        val longValueString = "$toLong"
        val cmTime = CMTimeMakeWithSeconds(longValueString.toDouble(), NSEC_PER_SEC.toInt())
        iosPlayer.currentItem?.seekToTime(time = cmTime, completionHandler = {
        })
    }


    fun addSongsUrls(songsUrl: List<String>) {
        //TODO: report bad url
        val converted = songsUrl.map {
            NSURL.URLWithString(URLString = it)!!
        }
        playerItems.addAll(converted.map { AVPlayerItem(uRL = it) })
    }

    private fun setUpAudioSession() {
        try {
            val audioSession = AVAudioSession.sharedInstance()
            audioSession.setCategory(AVAudioSessionCategoryPlayback, null)
            audioSession.setActive(true, null)
        } catch (e: Exception) {
            println("Error setting up audio session: ${e.message}")
        }
    }

    private fun startTimeObserver() {
        val interval = CMTimeMakeWithSeconds(1.0, NSEC_PER_SEC.toInt())
        timeObserver = iosPlayer.addPeriodicTimeObserverForInterval(interval, null, observer)
        NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = iosPlayer.currentItem,
            queue = NSOperationQueue.mainQueue,
            usingBlock = {
            }
        )
    }

    private fun stop() {
        if (::timeObserver.isInitialized) iosPlayer.removeTimeObserver(timeObserver)
        iosPlayer.pause()
        iosPlayer.currentItem?.seekToTime(CMTimeMakeWithSeconds(0.0, NSEC_PER_SEC.toInt()))
    }

    private fun playWithIndex(currentItemIndex: Int) {
        stop()
        startTimeObserver()

        val playItem = playerItems[currentItemIndex]
        iosPlayer.replaceCurrentItemWithPlayerItem(playItem)
        iosPlayer.play()
    }

    actual fun isInit(): Boolean {
        return iosPlayer.currentItem?.isPlaybackLikelyToKeepUp() == true
    }

    actual fun seekBack() {
    }

    actual fun seekForward() {
    }


}
