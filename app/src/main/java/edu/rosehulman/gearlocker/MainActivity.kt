package edu.rosehulman.gearlocker

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import edu.rosehulman.gearlocker.dashboard.DashboardFragment
import edu.rosehulman.gearlocker.inventory.InventoryFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        nav_view.setOnNavigationItemSelectedListener(this)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_management, R.id.navigation_inventory, R.id.navigation_messages, R.id.navigation_dashboard))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        var switchTo: Fragment? = null
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                switchTo = DashboardFragment()
            }
            R.id.navigation_messages -> {
            }
            R.id.navigation_inventory -> {
                switchTo = InventoryFragment()
            }
            R.id.navigation_management->{

            }
        }
        switchTo?.let { setFragment(it) }
        return true

    }

    private fun setFragment(switchTo: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.nav_host_fragment, switchTo)
        while (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        }
        ft.commit()
    }


//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }

}