package edu.rosehulman.gearlocker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.rosehulman.gearlocker.models.Club

class ManagementMainActivity: AppCompatActivity(), AuthProvider, ClubProvider {
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var clubID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.management_activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view_management)
        val navController = findNavController(R.id.nav_host_fragment_management)
        Log.d(Constants.TAG, "On create for management fragment")
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_management_rentals, R.id.navigation_management_inventory, R.id.navigation_management_messages, R.id.navigation_management_dashboard))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val args: ManagementMainActivityArgs by navArgs()
        this.clubID = args.clubID

        navController.addOnDestinationChangedListener { _, _, _ ->
            navView.isVisible = true
        }
    }

    override fun getUID(): String {
        return clubID
    }

    override fun getActiveClubID(): String {
        return clubID
    }
}