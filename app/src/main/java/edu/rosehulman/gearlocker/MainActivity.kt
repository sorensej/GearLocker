package edu.rosehulman.gearlocker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import edu.rosehulman.rosefire.Rosefire


class MainActivity : AppCompatActivity(){
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val auth = FirebaseAuth.getInstance()
    private var authListener: FirebaseAuth.AuthStateListener? = null

    private val RC_ROSEFIRE_LOGIN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.renter_activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.isVisible = true
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_management, R.id.navigation_inventory, R.id.navigation_messages, R.id.navigation_dashboard))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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
                val rosefireIntent = Rosefire.getSignInIntent(this, getString(R.string.rosefire_token))
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