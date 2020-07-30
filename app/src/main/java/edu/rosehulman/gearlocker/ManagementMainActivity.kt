package edu.rosehulman.gearlocker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.management_activity_main.*

class ManagementMainActivity: AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.management_activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view_management)
        val navController = findNavController(R.id.nav_host_fragment_management)

        navView.setOnNavigationItemSelectedListener {
            Log.d(Constants.TAG, "navigation item selected")
            if (it.itemId == R.id.navigation_management_dashboard){
                startActivity(Intent(this, MainActivity::class.java))
            }
            true
        }
        Log.d(Constants.TAG, "On create for management fragment")
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_management_rentals, R.id.navigation_management_inventory, R.id.navigation_management_messages, R.id.navigation_management_dashboard))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


}