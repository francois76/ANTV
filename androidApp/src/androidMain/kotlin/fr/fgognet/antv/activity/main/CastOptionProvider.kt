package fr.fgognet.antv.activity.main

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.cast.CastMediaControlIntent
import com.google.android.gms.cast.framework.*

// CastOptionsProvider provided in manifest, do not remove
@Suppress("unused")
class CastOptionsProvider : OptionsProvider {

    @SuppressLint("VisibleForTests")
    override fun getCastOptions(context: Context): CastOptions {
        return CastOptions.Builder()
            .setResumeSavedSession(false)
            .setEnableReconnectionService(false)
            .setReceiverApplicationId(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID)
            .setStopReceiverApplicationWhenEndingSession(true)
            .build()
    }

    override fun getAdditionalSessionProviders(context: Context): List<SessionProvider> {
        return emptyList()
    }


}