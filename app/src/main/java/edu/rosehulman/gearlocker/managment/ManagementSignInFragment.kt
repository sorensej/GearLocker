package edu.rosehulman.gearlocker.managment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.rosehulman.gearlocker.ManagementMainActivity
import edu.rosehulman.gearlocker.R
import kotlinx.android.synthetic.main.gear_management_sign_in.view.*

class ManagementSignInFragment: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : RelativeLayout =
            inflater.inflate(R.layout.gear_management_sign_in, container, false) as RelativeLayout
        var bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView?.isVisible = false
        view.manage_gear_button.setOnClickListener {
            startActivity(Intent(this.context, ManagementMainActivity::class.java))
        }
        view.return_to_dash_button_login.setOnClickListener {
            bottomNavigationView?.isVisible = true
            findNavController().navigate(R.id.navigation_dashboard)
        }
        return view
    }
}