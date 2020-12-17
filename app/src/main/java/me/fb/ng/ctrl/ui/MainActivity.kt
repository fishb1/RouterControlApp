package me.fb.ng.ctrl.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import me.fb.ng.ctrl.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Connect navView view nav graph
        val navFragment = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navView.setupWithNavController(navFragment.navController)
    }

    fun toggleDrawer() {
        if (drawer.isOpen) {
            drawer.close()
        } else {
            drawer.open()
        }
    }
}
