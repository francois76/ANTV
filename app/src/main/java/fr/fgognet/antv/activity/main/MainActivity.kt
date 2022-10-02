package fr.fgognet.antv.activity.main

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.media3.common.util.UnstableApi
import com.google.android.gms.cast.framework.CastContext
import fr.fgognet.antv.config.initCommonLogs
import fr.fgognet.antv.view.main.ANTVApp

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
private const val TAG = "ANTV/MainActivity"

open class MainActivity : FragmentActivity() {

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        initCommonLogs()
        setContent {
            ANTVApp()
        }
        CastContext.getSharedInstance(applicationContext)
    }

}