package edu.rosehulman.gearlocker.rentals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.R
import kotlinx.android.synthetic.main.management_activity_main.*

class RentalsOverviewManagment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val recyclerView : RecyclerView =
            inflater.inflate(R.layout.fragment_rental_overview, container, false) as RecyclerView
        activity?.nav_host_fragment_management?.findNavController()?.graph?.startDestination = R.id.rentalsOverviewManagment
        return recyclerView
    }
}