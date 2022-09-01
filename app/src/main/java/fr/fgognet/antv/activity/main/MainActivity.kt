package fr.fgognet.antv.activity.main

import android.app.PictureInPictureParams
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.Menu
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fr.fgognet.antv.R
import fr.fgognet.antv.config.initCommonLogs
import fr.fgognet.antv.jetpackView.main.ANTVApp
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.utils.linkifyHtml

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
private const val TAG = "ANTV/MainActivity"

open class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        initCommonLogs()
        setContent {
            ANTVApp()
        }
        CastContext.getSharedInstance(applicationContext)
        PlayerService.init(application)
        val topMenu = findViewById<MaterialToolbar>(R.id.topAppBar).menu
        CastButtonFactory.setUpMediaRouteButton(
            applicationContext,
            topMenu,
            R.id.media_route_menu_item
        )
        val s = linkifyHtml(resources.getString(R.string.credits), Linkify.ALL)
        topMenu.findItem(R.id.info_menu_item).setOnMenuItemClickListener {
            val dialog = MaterialAlertDialogBuilder(
                this,
                com.google.android.material.R.style.MaterialAlertDialog_Material3
            )
                .setTitle(resources.getString(R.string.info))
                .setMessage(
                    s
                )
                .show()
            (dialog.findViewById<TextView>(android.R.id.message))?.movementMethod =
                LinkMovementMethod.getInstance()
            true
        }
    }

    private var listenerKey: Int = 0


    override fun onResume() {
        Log.v(TAG, "onResume")
        super.onResume()
        listenerKey = PlayerService.registerListener(ActivityPlayerListener(this))
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.v(TAG, "onCreateOptionsMenu")
        val result = super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.browse, menu)
        return result
    }


    override fun onPause() {
        Log.v(TAG, "onPause")
        PlayerService.unregisterListener(listenerKey)
        // if we are in picture in picture mode, we need to navigate to the player
        if (isInPictureInPictureMode) {
/*            if (navHostFragment.navController.currentDestination?.id != R.id.playerFragment) {
                navigate(R.id.playerFragment)
            }*/
        }
        super.onPause()
    }

    override fun enterPictureInPictureMode(params: PictureInPictureParams): Boolean {
        Log.v(TAG, "enterPictureInPictureMode")
        return super.enterPictureInPictureMode(params)
    }

    override fun onDestroy() {
        Log.v(TAG, "onDestroy")
        if (this.isFinishing) {
            PlayerService.release()
        }
        super.onDestroy()
    }
}