package edu.rosehulman.gearlocker.management


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.MainActivity

import edu.rosehulman.gearlocker.R


class ManagementHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : ConstraintLayout =
            inflater.inflate(R.layout.gear_management_home, container, false) as ConstraintLayout
        var bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view_management)
        bottomNavigationView?.isVisible = false
        view.getViewById(R.id.checkin_button).setOnClickListener {
            Log.d(Constants.TAG, "Inventory clicked from management page")
            bottomNavigationView?.isVisible = true
            findNavController().navigate(R.id.navigation_management_inventory)
        }
        view.getViewById(R.id.rentals_button).setOnClickListener{
            bottomNavigationView?.isVisible = true
            findNavController().navigate(R.id.navigation_management_rentals)
        }
        view.getViewById(R.id.checkout_button).setOnClickListener {
            bottomNavigationView?.isVisible = true
            findNavController().navigate(R.id.navigation_management_messages)
        }
        view.getViewById(R.id.return_to_dash_button).setOnClickListener {
            startActivity(Intent(this.context, MainActivity::class.java))
        }
        return view
    }
}