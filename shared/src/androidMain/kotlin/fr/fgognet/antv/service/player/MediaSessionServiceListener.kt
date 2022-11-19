package fr.fgognet.antv.service.player

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.media3.cast.SessionAvailabilityListener
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import fr.fgognet.antv.external.editorial.Diffusion
import fr.fgognet.antv.external.editorial.Editorial
import fr.fgognet.antv.external.editorial.EditorialRepository
import fr.fgognet.antv.external.eventSearch.EventSearch
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.external.eventSearch.EventSearchRepository
import fr.fgognet.antv.external.live.LiveRepository
import fr.fgognet.antv.external.nvs.NvsRepository
import fr.fgognet.antv.utils.cleanDescription
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.guava.asListenableFuture
import kotlinx.coroutines.supervisorScope
import java.util.*

private const val TAG = "ANTV/MediaSessionServiceListener"

@UnstableApi
class MediaSessionServiceListener(private val service: MediaSessionServiceImpl) :
    SessionAvailabilityListener,
    MediaLibraryService.MediaLibrarySession.Callback {


    override fun onCastSessionAvailable() {
        Log.v(TAG, "onCastSessionAvailable")
        service.cast()
    }


    override fun onCastSessionUnavailable() {
        Log.v(TAG, "onCastSessionUnavailable")
        service.stopCast()
    }


    @UnstableApi
    override fun onGetLibraryRoot(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
        Log.v(TAG, "onGetLibraryRoot")
        val result = LibraryResult.ofItem(
            MediaItem.Builder().setMediaId("root")
                .setMimeType(MimeTypes.BASE_TYPE_APPLICATION).setMediaMetadata(
                    MediaMetadata.Builder().setFolderType(MediaMetadata.FOLDER_TYPE_NONE)
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
        Log.v(TAG, "onSearch")
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
        Log.v(TAG, "onGetSearchResult")
        return runAsyncMediacall(Bundle().apply {
            putInt(
                "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT",
                2
            )
        }) {
            handleReplay()
        }
    }


    @UnstableApi
    override fun onGetChildren(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        Log.v(TAG, "onGetChildren")
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
                Log.e(TAG, parentId)
                super.onGetChildren(session, browser, parentId, page, pageSize, params)
            }
        }
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        Log.v(TAG, "onAddMediaItems")
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
                Log.e(TAG, t.stackTraceToString())
            }
        }
        Futures.addCallback(future, callBack, MoreExecutors.directExecutor())
        return future
    }


    @UnstableApi
    private fun handleRoot(): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        Log.v(TAG, "handleRoot")
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

    @UnstableApi
    private fun runAsyncMediacall(
        extra: Bundle,
        function: suspend () -> ArrayList<MediaItem>
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        Log.v(TAG, "runAsyncMediacall")
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
                Log.e(TAG, t.stackTraceToString())
            }
        }
        Futures.addCallback(future, callBack, MoreExecutors.directExecutor())
        return future
    }


    @UnstableApi
    private suspend fun handleLive(): ArrayList<MediaItem> {
        Log.v(TAG, "handleLive")
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
                                                .setFolderType(MediaMetadata.FOLDER_TYPE_NONE)
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


    @UnstableApi
    private suspend fun handleReplay(): ArrayList<MediaItem> {
        Log.v(TAG, "handleReplay")
        val itemResult: ArrayList<MediaItem> = arrayListOf()
        supervisorScope {
            val eventSearches: List<EventSearch> = try {
                EventSearchRepository.findEventSearchByParams(
                    hashMapOf<EventSearchQueryParams, String>()
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
                            .setFolderType(MediaMetadata.FOLDER_TYPE_NONE)
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

    @UnstableApi
    private fun buildFolder(mediaId: String, title: String): MediaItem {
        Log.v(TAG, "buildFolder")
        return MediaItem.Builder()
            .setMimeType(MimeTypes.BASE_TYPE_APPLICATION).setMediaId(mediaId)
            .setMediaMetadata(
                MediaMetadata.Builder().setFolderType(MediaMetadata.FOLDER_TYPE_TITLES)
                    .setIsPlayable(false)
                    .setTitle(title)
                    .build()
            ).build()
    }

    companion object {
        var currentItems: Map<String, MediaItem> = hashMapOf()
    }

}