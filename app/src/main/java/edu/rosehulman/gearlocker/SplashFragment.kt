package edu.rosehulman.gearlocker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.rosehulman.gearlocker.models.Cart
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.synthetic.main.splash_fragment.view.*

class SplashFragment : Fragment() {
    private var listener: ApplicationNavigationListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.splash_fragment, container, false)
        var bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView?.isVisible = false
        view.login_button_rosefire.setOnClickListener {
            listener?.onRoseLoginPressed()
        }
        view.login_email_button.setOnClickListener {
            listener?.onLoginButtonPressed()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ApplicationNavigationListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnLoginButtonPressedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface ApplicationNavigationListener {
        fun onLoginButtonPressed()
        fun onRoseLoginPressed()
        fun onCartItemAdded(item: Item)
        fun onGetCart(): Cart
        fun onGetNavController(): NavController
    }
}