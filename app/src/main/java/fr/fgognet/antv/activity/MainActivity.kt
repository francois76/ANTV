package fr.fgognet.antv.activity

import android.app.PictureInPictureParams
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.Menu
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationBarView
import fr.fgognet.antv.R


private const val TAG = "ANTV/MainActivity"

/**
 * MainActivity activity that hold the navigation
 */
open class MainActivity : FragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val topMenu = findViewById<MaterialToolbar>(R.id.topAppBar).menu
        CastButtonFactory.setUpMediaRouteButton(
            applicationContext,
            topMenu,
            R.id.media_route_menu_item
        )
        val s = SpannableString(resources.getString(R.string.credits))
        Linkify.addLinks(s, Linkify.ALL)
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
                navHostFragment.navController.navigate(R.id.mainFragment, Bundle())
                bottomMenu.findItem(R.id.menu_replay_id).isChecked = false
                it.isChecked = true
                true
            }
        bottomMenu.findItem(R.id.menu_replay_id)
            .setOnMenuItemClickListener {
                navHostFragment.navController.navigate(R.id.replaySearchFragment, Bundle())
                bottomMenu.findItem(R.id.menu_live_id).isChecked = false
                it.isChecked = true
                true
            }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val builder = PictureInPictureParams.Builder()
            builder.setAutoEnterEnabled(true)
            this.setPictureInPictureParams(builder.build())
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
        super.onPause()
    }

    override fun enterPictureInPictureMode(params: PictureInPictureParams): Boolean {
        Log.v(TAG, "enterPictureInPictureMode")
        return super.enterPictureInPictureMode(params)
    }


}