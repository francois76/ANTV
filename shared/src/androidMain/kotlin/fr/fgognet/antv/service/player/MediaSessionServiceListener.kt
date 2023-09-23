package fr.fgognet.antv.service.player

import android.net.Uri
import android.os.Bundle
import androidx.media3.cast.SessionAvailabilityListener
import androidx.media3.common.*
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.*
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.*
import fr.fgognet.antv.external.editorial.*
import fr.fgognet.antv.external.eventSearch.EventSearch
import fr.fgognet.antv.external.eventSearch.EventSearchRepository
import fr.fgognet.antv.external.live.LiveRepository
import fr.fgognet.antv.external.nvs.NvsRepository
import fr.fgognet.antv.utils.cleanDescription
import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.guava.asListenableFuture
import java.util.UUID

private const val TAG = "ANTV/MediaSessionServiceListener"

@UnstableApi
class MediaSessionServiceListener(private val service: MediaSessionServiceImpl) :
    SessionAvailabilityListener,
    MediaLibraryService.MediaLibrarySession.Callback {


    override fun onCastSessionAvailable() {
        Napier.v(tag = TAG, message = "onCastSessionAvailable")
        service.cast()
    }


    override fun onCastSessionUnavailable() {
        Napier.v(tag = TAG, message = "onCastSessionUnavailable")
        service.stopCast()
    }


    override fun onGetLibraryRoot(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
        Napier.v(tag = TAG, message = "onGetLibraryRoot")
        val result = LibraryResult.ofItem(
            MediaItem.Builder().setMediaId("root")
                .setMimeType(MimeTypes.BASE_TYPE_APPLICATION).setMediaMetadata(
                    MediaMetadata.Builder().setIsBrowsable(false)
                        .setIsPlayable(false).build()
                ).build(), null
        )
        return Futures.immediateFuture(result)
    }

    override fun onSearch(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        query: String,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<Void>> {
        Napier.v(tag = TAG, message = "onSearch")
        session.notifySearchResultChanged(browser, query, 0, params)
        return Futures.immediateFuture(
            LibraryResult.ofVoid(
                MediaLibraryService.LibraryParams.Builder().setRecent(true).build()
            )
        )
    }

    override fun onGetSearchResult(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        query: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        Napier.v(tag = TAG, message = "onGetSearchResult")
        return runAsyncMediacall(Bundle().apply {
            putInt(
                "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT",
                2
            )
        }) {
            handleReplay()
        }
    }


    override fun onGetChildren(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        Napier.v(tag = TAG, message = "onGetChildren")
        return when (parentId) {
            "root" -> handleRoot()
            "live" -> runAsyncMediacall(Bundle().apply {
                putInt(
                    "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT",
                    2
                )
            }) {
                handleLive()
            }

            "replay" -> runAsyncMediacall(Bundle().apply {
                putInt(
                    "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT",
                    2
                )
            }) {
                handleReplay()
            }

            else -> {
                Napier.e(tag = TAG, message = parentId)
                super.onGetChildren(session, browser, parentId, page, pageSize, params)
            }
        }
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        Napier.v(tag = TAG, message = "onAddMediaItems")
        var item = currentItems[mediaItems[0].mediaId]
        item = item?.buildUpon()?.setMimeType(MimeTypes.APPLICATION_M3U8)?.build()
        if (mediaItems.size != 1 || item == null) {
            return Futures.immediateFuture(
                arrayListOf()
            )
        }
        if (item.mediaId == mediaSession.player.currentMediaItem?.mediaId) {
            return super.onAddMediaItems(mediaSession, controller, mediaItems)
        }
        if (item.localConfiguration?.uri != null) {
            MediaSessionServiceImpl.currentMediaItem = item
            return Futures.immediateFuture(
                arrayListOf(item)
            )
        }
        val future = MainScope().async {
            val nvs = NvsRepository.getNvsByCode(
                item.mediaId
            )

            mutableListOf(
                item.buildUpon()
                    .setUri(nvs.getReplayURL())
                    .build()
            )
        }.asListenableFuture()
        val callBack = object : FutureCallback<MutableList<MediaItem>> {
            override fun onSuccess(result: MutableList<MediaItem>?) {
                MediaSessionServiceImpl.currentMediaItem = item
            }

            override fun onFailure(t: Throwable) {
                Napier.e(tag = TAG, message = t.stackTraceToString())
            }
        }
        Futures.addCallback(future, callBack, MoreExecutors.directExecutor())
        return future
    }


    private fun handleRoot(): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        Napier.v(tag = TAG, message = "handleRoot")
        return Futures.immediateFuture(
            LibraryResult.ofItemList(
                arrayListOf(
                    buildFolder(mediaId = "live", title = "Live"),
                    buildFolder(mediaId = "replay", title = "Replay"),
                ),
                MediaLibraryService.LibraryParams.Builder().setExtras(
                    Bundle().apply {
                        putInt(
                            "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT",
                            2
                        )
                    }
                ).build()
            )
        )
    }

    private fun runAsyncMediacall(
        extra: Bundle,
        function: suspend () -> ArrayList<MediaItem>
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        Napier.v(tag = TAG, message = "runAsyncMediacall")
        val future = MainScope().async {
            val items = function()
            currentItems = items.associateBy {
                it.mediaId
            }
            LibraryResult.ofItemList(
                items, MediaLibraryService.LibraryParams.Builder().setExtras(
                    extra
                ).build()
            )
        }.asListenableFuture()
        val callBack = object : FutureCallback<LibraryResult<ImmutableList<MediaItem>>> {
            override fun onSuccess(result: LibraryResult<ImmutableList<MediaItem>>?) {}

            override fun onFailure(t: Throwable) {
                Napier.e(tag = TAG, message = t.stackTraceToString())
            }
        }
        Futures.addCallback(future, callBack, MoreExecutors.directExecutor())
        return future
    }


    private suspend fun handleLive(): ArrayList<MediaItem> {
        Napier.v(tag = TAG, message = "handleLive")
        val itemResult: ArrayList<MediaItem> = arrayListOf()
        supervisorScope {
            val editorial: Editorial? = try {
                EditorialRepository.getEditorialInformation()
            } catch (e: Exception) {
                null
            }
            if (editorial != null) {
                val liveInformation: Map<String, String> =
                    LiveRepository.getLiveInformation()
                if (editorial.diffusions != null) {
                    for (d in editorial.diffusions) {
                        val diffusion = d as Diffusion
                        if (liveInformation.containsKey(diffusion.flux)) {
                            val nvs = NvsRepository.getNvsByCode(
                                liveInformation[diffusion.flux]!!
                            )
                            if (liveInformation.containsKey(diffusion.flux) && diffusion.uid_referentiel == nvs.getMeetingID()) {
                                itemResult.add(
                                    MediaItem.Builder()
                                        .setMimeType(MimeTypes.APPLICATION_M3U8)
                                        .setMediaId(diffusion.uid_referentiel ?: "0")
                                        .setUri(Uri.parse("https://videos.assemblee-nationale.fr/live/live${diffusion.flux}/playlist${diffusion.flux}.m3u8"))
                                        .setMediaMetadata(
                                            MediaMetadata.Builder()
                                                .setIsPlayable(true)
                                                .setIsBrowsable(false)
                                                .setTitle(
                                                    diffusion.libelle
                                                ).setSubtitle(diffusion.lieu ?: "")
                                                .setDescription(
                                                    cleanDescription(diffusion.sujet)
                                                        ?: ""
                                                )
                                                .setArtworkUri(
                                                    Uri.parse(if (diffusion.id_organe != null) "https://videos.assemblee-nationale.fr/live/images/" + diffusion.id_organe + ".jpg" else "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg")
                                                )

                                                .build()
                                        ).build()
                                )
                            }
                        }
                    }
                }
            }
        }
        return itemResult
    }


    private suspend fun handleReplay(): ArrayList<MediaItem> {
        Napier.v(tag = TAG, message = "handleReplay")
        val itemResult: ArrayList<MediaItem> = arrayListOf()
        supervisorScope {
            val eventSearches: List<EventSearch> = try {
                EventSearchRepository.findEventSearchByParams(
                    hashMapOf()
                )
            } catch (e: Exception) {
                arrayListOf()
            }

            itemResult.addAll(eventSearches.map {
                MediaItem.Builder()
                    .setMimeType(MimeTypes.APPLICATION_M3U8)
                    .setMediaId(it.url ?: UUID.randomUUID().toString())
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setIsPlayable(true)
                            .setIsBrowsable(false)
                            .setTitle(
                                it.title
                            )
                            .setDescription(
                                cleanDescription(it.description)
                                    ?: ""
                            )
                            .setArtworkUri(
                                Uri.parse(
                                    if (it.thumbnail != null) it.thumbnail!!.replace(
                                        "\\",
                                        ""
                                    ).replace(
                                        "http",
                                        "https"
                                    ) else "https://videos.assemblee-nationale.fr/Datas/an/12053682_62cebe5145c82/files/S%C3%A9ance.jpg"
                                )
                            )

                            .build()
                    ).build()
            })
        }
        return itemResult

    }

    private fun buildFolder(mediaId: String, title: String): MediaItem {
        Napier.v(tag = TAG, message = "buildFolder")
        return MediaItem.Builder()
            .setMimeType(MimeTypes.BASE_TYPE_APPLICATION).setMediaId(mediaId)
            .setMediaMetadata(
                MediaMetadata.Builder().setIsBrowsable(true)
                    .setIsPlayable(false)
                    .setTitle(title)
                    .build()
            ).build()
    }

    companion object {
        var currentItems: Map<String, MediaItem> = hashMapOf()
    }

}