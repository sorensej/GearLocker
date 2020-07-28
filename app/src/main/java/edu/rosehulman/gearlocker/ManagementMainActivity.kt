package edu.rosehulman.gearlocker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.rosehulman.gearlocker.rentals.ManagementHomeFragment

class ManagementMainActivity: AppCompatActivity(), ManagementHomeFragment.FragmentSwitcher {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.management_activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view_management)
        val navController = findNavController(R.id.nav_host_fragment_management)
        Log.d(Constants.TAG, "On create for management fragment")
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_management_rentals, R.id.navigation_management_inventory, R.id.navigation_management_messages, R.id.navigation_management_dashboard))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setFragment(switchTo: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.nav_host_fragment, switchTo)
        while (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        }
        ft.commit()
    }

    override fun switchToFragment(fragment: Fragment) {
        setFragment(fragment)
    }
}