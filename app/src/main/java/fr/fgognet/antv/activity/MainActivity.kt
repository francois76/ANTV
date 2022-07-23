package fr.fgognet.antv.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.material.appbar.MaterialToolbar
import fr.fgognet.antv.R


val TAG = "ANTV/MainActivity"

/**
 * MainActivity activity that hold the navigation
 */
class MainActivity : FragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LOW_PROFILE)
        setContentView(R.layout.activity_main)
        CastButtonFactory.setUpMediaRouteButton(
            applicationContext,
            findViewById<MaterialToolbar>(R.id.topAppBar).menu,
            R.id.media_route_menu_item
        )

    }


    /**
     * We need to populate the Cast button across all activities as suggested by Google Cast Guide:
     * https://developers.google.com/cast/docs/design_checklist/cast-button#sender-cast-icon-available
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.v(TAG, "onCreateOptionsMenu")
        val result = super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.browse, menu)

        return result
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "onSaveInstanceState")
        super.onSaveInstanceState(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        savedInstanceState.putBundle(
            "nav_state",
            navController.saveState()
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.restoreState(savedInstanceState.getBundle("nav_state"))
    }


}