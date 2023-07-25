package fr.fgognet.antv.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import fr.fgognet.antv.BuildConfig
import io.github.aakira.napier.*

fun initAndroidLog() {
    if (BuildConfig.DEBUG) {
        // Debug build

        // disable firebase crashlytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
        // init napier
        Napier.base(DebugAntilog())
    } else {

        // enable firebase crashlytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        // init napier
        Napier.base(CrashlyticsAntilog())
    }
}

class CrashlyticsAntilog : Antilog() {

    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        if (priority < LogLevel.ERROR) return
        throwable?.let {
            FirebaseCrashlytics.getInstance().recordException(it)
        }
    }
}