package fr.fgognet.antv.activity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import fr.fgognet.antv.R


val TAG = "ANTV/MainActivity"

/**
 * MainActivity activity that hold the navigation
 */
class MainActivity : FragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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