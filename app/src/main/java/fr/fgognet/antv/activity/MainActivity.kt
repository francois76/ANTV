package fr.fgognet.antv.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import fr.fgognet.antv.R

/**
 * MainActivity activity that hold the navigation
 */
class MainActivity : FragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


}