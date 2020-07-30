package edu.rosehulman.gearlocker.rentals

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import edu.rosehulman.gearlocker.R


class RentalHomeFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : ConstraintLayout =
            inflater.inflate(R.layout.rental_home_fragment, container, false) as ConstraintLayout
        view.getViewById(R.id.overview_button).setOnClickListener {
            findNavController().navigate(R.id.rentalsOverviewManagment)
        }
        view.getViewById(R.id.checkin_button).setOnClickListener {
            findNavController().navigate(R.id.checkInFragment)
        }
        view.getViewById(R.id.checkout_button).setOnClickListener {
            findNavController().navigate(R.id.checkOutFragment)
        }
        view.getViewById(R.id.forms_button).setOnClickListener {

        }

        return view
    }
}