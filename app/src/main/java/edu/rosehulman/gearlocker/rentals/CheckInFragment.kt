package edu.rosehulman.gearlocker.rentals

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import kotlinx.android.synthetic.main.check_in.view.*
import kotlinx.android.synthetic.main.management_activity_main.*
import kotlinx.android.synthetic.main.rental_home_fragment.view.*

class CheckInFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : RelativeLayout =
            inflater.inflate(R.layout.check_in, container, false) as RelativeLayout
        view.cancel_check_in_button.setOnClickListener {
            findNavController().navigate(R.id.navigation_management_rentals)
        }
        view.complete_check_in_button.setOnClickListener {
            var builder = AlertDialog.Builder(this.context)
            builder.setTitle("Check-In Complete")
            Log.d(Constants.TAG, "Item checked in")
            builder.setPositiveButton("Return to Rentals"){ _, _ ->
                Log.d(Constants.TAG, "Checkin complete")
                findNavController().navigate(R.id.navigation_management_rentals)
            }
            builder.setNeutralButton("Return to Managment Home"){ _, _ ->
                Log.d(Constants.TAG, "Checkin complete")
                findNavController().navigate(R.id.navigation_management_home)
            }
            builder.create().show()
        }
        activity?.nav_host_fragment_management?.findNavController()?.graph?.startDestination = R.id.checkInFragment
        return view
    }
}