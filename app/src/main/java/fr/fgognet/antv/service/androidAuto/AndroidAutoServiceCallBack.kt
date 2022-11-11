package fr.fgognet.antv.service.androidAuto

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.LibraryParams
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import fr.fgognet.antv.external.editorial.Diffusion
import fr.fgognet.antv.external.editorial.Editorial
import fr.fgognet.antv.external.editorial.EditorialRepository
import fr.fgognet.antv.external.eventSearch.EventSearch
import fr.fgognet.antv.external.eventSearch.EventSearchQueryParams
import fr.fgognet.antv.external.eventSearch.EventSearchRepository
import fr.fgognet.antv.external.live.LiveRepository
import fr.fgognet.antv.external.nvs.NvsRepository
import fr.fgognet.antv.utils.cleanDescription
import kotlinx.coroutines.*
import kotlinx.coroutines.guava.asListenableFuture
import java.util.*

class AndroidAutoServiceCallBack : MediaLibraryService.MediaLibrarySession.Callback {

    // TAG
    private val TAG = "ANTV/AndroidAutoServiceCallBack"

    @UnstableApi
    override fun onGetLibraryRoot(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: LibraryParams?
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


    @UnstableApi
    override fun onGetChildren(
        session: MediaLibraryService.MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        Log.v(TAG, "onGetChildren")
        return when (parentId) {
            "root" -> handleRoot()
            "live" -> handleLive()
            "replay" -> handleReplay()
            else -> handleRoot()
        }

    }

    @UnstableApi
    private fun handleRoot(): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        return Futures.immediateFuture(
            LibraryResult.ofItemList(
                arrayListOf(
                    buildFolder(mediaId = "live", title = "Live"),
                    buildFolder(mediaId = "replay", title = "Replay"),
                ),
                LibraryParams.Builder().setExtras(
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


    @OptIn(ExperimentalCoroutinesApi::class)
    @UnstableApi
    private fun handleLive(): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {

        return MainScope().async {
            val itemResult: ArrayList<MediaItem> = arrayListOf()
            supervisorScope {
                withContext(Dispatchers.IO) {
                    val editorial: Editorial? = try {
                        EditorialRepository.getEditorialInformation()
                    } catch (e: Exception) {
                        Log.e(
                            TAG,
                            e.stackTraceToString(),
                        )
                        null
                    }
                    if (editorial != null) {
                        val liveInformation: Map<String, String> =
                            LiveRepository.getLiveInformation()
                        for (d in editorial.diffusions!!) {
                            try {
                                val diffusion = d as Diffusion
                                if (liveInformation.containsKey(diffusion.flux)) {
                                    val nvs = NvsRepository.getNvsByCode(
                                        liveInformation[diffusion.flux]!!
                                    )
                                    if (liveInformation.containsKey(diffusion.flux) && diffusion.uid_referentiel == nvs.getMeetingID()) {
                                        withContext(Dispatchers.Main) {
                                            itemResult.add(
                                                MediaItem.Builder()
                                                    .setMimeType(MimeTypes.APPLICATION_M3U8)
                                                    .setMediaId(diffusion.uid_referentiel ?: "0")
                                                    .setUri(Uri.parse("https://videos.assemblee-nationale.fr/live/live${diffusion.flux}/playlist${diffusion.flux}.m3u8"))
                                                    .setMediaMetadata(
                                                        MediaMetadata.Builder()
                                                            .setIsPlayable(true)
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

                            } catch (e: Exception) {
                                Log.e(
                                    TAG, e.stackTraceToString()
                                )
                            }
                        }
                    }
                }

            }
            LibraryResult.ofItemList(itemResult, LibraryParams.Builder().setExtras(
                Bundle().apply {
                    putInt(
                        "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT",
                        2
                    )
                }
            ).build())
        }.asListenableFuture()

    }


    @UnstableApi
    private fun handleReplay(): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        return MainScope().async {
            val itemResult: ArrayList<MediaItem> = arrayListOf()
            supervisorScope {
                withContext(Dispatchers.IO) {
                    val eventSearches: List<EventSearch> = try {
                        EventSearchRepository.findEventSearchByParams(
                            hashMapOf<EventSearchQueryParams, String>()
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, e.toString())
                        arrayListOf()
                    }
                    withContext(Dispatchers.Main) {
                        Log.i(
                            TAG, "dispatching regenerated view"
                        )

                        itemResult.addAll(
                            eventSearches.map {
                                MediaItem.Builder()
                                    .setMimeType(MimeTypes.APPLICATION_M3U8)
                                    .setMediaId(UUID.randomUUID().toString())
                                    .setUri("TODO")
                                    .setMediaMetadata(
                                        MediaMetadata.Builder()
                                            .setIsPlayable(true)
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
                            }

                        )
                    }
                }

            }
            LibraryResult.ofItemList(itemResult, LibraryParams.Builder().setExtras(
                Bundle().apply {
                    putInt(
                        "android.media.browse.CONTENT_STYLE_BROWSABLE_HINT",
                        2
                    )
                }
            ).build())
        }.asListenableFuture()
    }

    @UnstableApi
    private fun buildFolder(mediaId: String, title: String): MediaItem {
        return MediaItem.Builder()
            .setMimeType(MimeTypes.BASE_TYPE_APPLICATION).setMediaId(mediaId)
            .setMediaMetadata(
                MediaMetadata.Builder().setFolderType(MediaMetadata.FOLDER_TYPE_TITLES)
                    .setIsPlayable(false)
                    .setTitle(title)
                    .build()
            ).build()
    }
}