package fr.fgognet.antv.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.fgognet.antv.R


/**
 * MainActivity activity that hold the navigation
 */
open class MainActivity : FragmentActivity() {


    open val TAG = "ANTV/MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CastButtonFactory.setUpMediaRouteButton(
            applicationContext,
            findViewById<MaterialToolbar>(R.id.topAppBar).menu,
            R.id.media_route_menu_item
        )
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        findViewById<BottomNavigationView>(R.id.bottom_navigation).menu.findItem(R.id.menu_live_id)
            .setOnMenuItemClickListener {
                navHostFragment.navController.navigate(R.id.mainFragment, Bundle())
                true
            }
        findViewById<BottomNavigationView>(R.id.bottom_navigation).menu.findItem(R.id.menu_replay_id)
            .setOnMenuItemClickListener {
                navHostFragment.navController.navigate(R.id.replaySearchFragment, Bundle())
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
        Log.d(TAG, "onSaveInstanceState")
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
        Log.d(TAG, "onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.restoreState(savedInstanceState.getBundle("nav_state"))
        findViewById<MaterialToolbar>(R.id.topAppBar).title = savedInstanceState.getString("title")
    }


}