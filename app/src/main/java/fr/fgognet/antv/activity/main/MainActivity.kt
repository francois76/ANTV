package fr.fgognet.antv.activity.main

import android.app.PictureInPictureParams
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.Menu
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationBarView
import fr.fgognet.antv.R
import fr.fgognet.antv.config.initCommonLogs
import fr.fgognet.antv.service.player.PlayerService
import fr.fgognet.antv.utils.linkifyHtml


private const val TAG = "ANTV/MainActivity"

/**
 * MainActivity activity that hold the navigation
 *
 *
 *
 */
open class MainActivity : FragmentActivity() {

    private var listenerKey: Int = 0


    override fun onResume() {
        Log.v(TAG, "onResume")
        super.onResume()
        listenerKey = PlayerService.registerListener(ActivityPlayerListener(this))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        initCommonLogs()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val bottomMenu = findViewById<NavigationBarView>(R.id.bottom_navigation).menu
        bottomMenu.findItem(R.id.menu_live_id)
            .setOnMenuItemClickListener {
                navHostFragment.navController.navigate(R.id.liveFragment, Bundle())
                bottomMenu.findItem(R.id.menu_search_id).isChecked = false
                bottomMenu.findItem(R.id.menu_playlist_id).isChecked = false
                it.isChecked = true
                true
            }
        bottomMenu.findItem(R.id.menu_search_id)
            .setOnMenuItemClickListener {
                navHostFragment.navController.navigate(R.id.replaySearchFragment, Bundle())
                bottomMenu.findItem(R.id.menu_live_id).isChecked = false
                bottomMenu.findItem(R.id.menu_playlist_id).isChecked = false
                it.isChecked = true
                true
            }
        bottomMenu.findItem(R.id.menu_playlist_id)
            .setOnMenuItemClickListener {
                navHostFragment.navController.navigate(R.id.playlistFragment, Bundle())
                bottomMenu.findItem(R.id.menu_live_id).isChecked = false
                bottomMenu.findItem(R.id.menu_search_id).isChecked = false
                it.isChecked = true
                true
            }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.v(TAG, "onCreateOptionsMenu")
        val result = super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.browse, menu)
        return result
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        Log.v(TAG, "onSaveInstanceState")
        super.onSaveInstanceState(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        savedInstanceState.putBundle(
            "nav_state",
            navController.saveState()
        )
        savedInstanceState.putString(
            "title",
            findViewById<MaterialToolbar>(R.id.topAppBar).title.toString()
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.v(TAG, "onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.restoreState(savedInstanceState.getBundle("nav_state"))
        findViewById<MaterialToolbar>(R.id.topAppBar).title = savedInstanceState.getString("title")
    }

    override fun onPause() {
        Log.v(TAG, "onPause")
        PlayerService.unregisterListener(listenerKey)
        // if we are in picture in picture mode, we need to navigate to the player
        if (isInPictureInPictureMode) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            if (navHostFragment.navController.currentDestination?.id != R.id.playerFragment) {
                navHostFragment.navController.navigate(R.id.playerFragment)
            }
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