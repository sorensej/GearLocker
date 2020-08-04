package edu.rosehulman.gearlocker

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import edu.rosehulman.rosefire.Rosefire


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val auth = FirebaseAuth.getInstance()
    private var authListener: FirebaseAuth.AuthStateListener? = null

    private val RC_ROSEFIRE_LOGIN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.renter_activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.isVisible = true
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_management,
                R.id.navigation_inventory,
                R.id.navigation_messages,
                R.id.navigation_dashboard
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.app_bar_menu)
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.add_club) {
                navController.navigate(R.id.placeholder)
            }
            true
        }
        setActionBar(toolbar)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            navView.isVisible = true
        }

        //initializeListeners()
    }

    override fun onStart() {
        super.onStart()
        if (authListener != null) {
            auth.addAuthStateListener(authListener!!)
        }
    }

    override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth.removeAuthStateListener(authListener!!)
        }
    }

    fun initializeListeners() {
        authListener = FirebaseAuth.AuthStateListener {
            val user = auth.currentUser
            if (user != null) {
                val uid = user.uid
                findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_dashboard)
            } else {
                val rosefireIntent =
                    Rosefire.getSignInIntent(this, getString(R.string.rosefire_token))
                startActivityForResult(rosefireIntent, RC_ROSEFIRE_LOGIN)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == RC_ROSEFIRE_LOGIN) {
            val result = Rosefire.getSignInResultFromIntent(data)
            if (!result.isSuccessful) {
                return
            }
            auth.signInWithCustomToken(result.token).addOnCompleteListener {
                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                }
            }
        }
    }


}