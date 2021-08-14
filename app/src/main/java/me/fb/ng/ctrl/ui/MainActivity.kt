package me.fb.ng.ctrl.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import me.fb.ng.ctrl.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navView: NavigationView
    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Init views
        navView = findViewById(R.id.navView)
        drawer = findViewById(R.id.drawer)
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
