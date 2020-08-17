package edu.rosehulman.gearlocker

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.models.Cart
import edu.rosehulman.gearlocker.models.Club
import edu.rosehulman.gearlocker.models.Item
import edu.rosehulman.rosefire.Rosefire


class MainActivity : AppCompatActivity(), SplashFragment.ApplicationNavigationListener, AuthProvider, ClubProvider {
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var cart: Cart = Cart()

    private val auth = FirebaseAuth.getInstance()
    private val signIn = 1
    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var uid: String = ""
    private var activeClubID: String = ""

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
        navView.setupWithNavController(navController)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.app_bar_menu)
        setActionBar(toolbar)
        navController.addOnDestinationChangedListener { _, _, _ ->
            navView.isVisible = true
        }

        initializeListeners()
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

    private fun initializeListeners() {
        authListener = FirebaseAuth.AuthStateListener {
            val user = auth.currentUser
            if (user != null) {
                uid = user.uid
                findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_dashboard)
            } else {
                findViewById<BottomNavigationView>(R.id.nav_view).isVisible = false
                findNavController(R.id.nav_host_fragment).navigate(R.id.splashFragment)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.switch_active_club -> {
                showClubSwitcher()
                true
            }
            R.id.add_club->{
                findNavController(R.id.nav_host_fragment).navigate(R.id.clubsFragment)
                true
            }
            R.id.log_out->{
                auth.signOut()
                initializeListeners()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onLoginButtonPressed() {
        launchLoginUI()
    }

    private fun launchLoginUI() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build())
        val loginIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        startActivityForResult(
            loginIntent,
            signIn
        )
    }

    private fun showClubSwitcher() {
        val builder = AlertDialog.Builder(this)
        FirebaseFirestore
            .getInstance()
            .collection(Constants.FB_CLUBS)
            .get().addOnSuccessListener { querySnapshot ->
                val clubMap = HashMap<String, String>()

                for (doc in querySnapshot.documents) {
                    val club = Club.fromSnapshot(doc)
                    clubMap[club.name] = club.id
                }

                val clubs = clubMap.keys.toTypedArray()

                builder.setItems(clubs) { _, which ->
                    activeClubID = clubMap[clubs[which]]!!
                }
                builder.create().show()
            }
    }

    override fun onRoseLoginPressed() {
        val signInIntent = Rosefire.getSignInIntent(this, getString(R.string.rosefire_token))
        startActivityForResult(signInIntent, RC_ROSEFIRE_LOGIN)
    }

    override fun onCartItemAdded(item: Item) {
        cart.arrayList.add(item)
    }

    override fun onGetCart() : Cart{
        return cart
    }

    override fun onGetNavController(): NavController {
        return findNavController(R.id.nav_host_fragment)
    }

    override fun getUID(): String {
        return uid
    }

    override fun getActiveClub(): String {
        return activeClubID
    }

}