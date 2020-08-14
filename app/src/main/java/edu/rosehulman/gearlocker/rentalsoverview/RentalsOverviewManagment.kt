package edu.rosehulman.gearlocker.rentalsoverview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Rental
import kotlinx.android.synthetic.main.fragment_rental_overview.view.*
import kotlinx.android.synthetic.main.management_activity_main.*

class RentalsOverviewManagment: Fragment(), RentalRequestViewHolder.RentalHandler{

    var curRentalsAdapter: CurrentRentalsAdapter? = null
    var rentalRequestAdapter: RentalRequestsAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val constraintView =
            inflater.inflate(R.layout.fragment_rental_overview, container, false) as ConstraintLayout
        activity?.nav_host_fragment_management?.findNavController()?.graph?.startDestination = R.id.rentalsOverviewManagment
        curRentalsAdapter = CurrentRentalsAdapter(context, findNavController())
        rentalRequestAdapter =
            RentalRequestsAdapter(context, this as RentalRequestViewHolder.RentalHandler)
        constraintView.cur_rentals_recyclerview.adapter = curRentalsAdapter
        constraintView.rental_requests_recyclerview.adapter = rentalRequestAdapter
        constraintView.cur_rentals_recyclerview.layoutManager = LinearLayoutManager(context)
        constraintView.rental_requests_recyclerview.layoutManager = LinearLayoutManager(context)
        return constraintView
    }

    override fun confirmRental(rental: Rental, position: Int) {
        curRentalsAdapter?.add(rental)
        rentalRequestAdapter?.remove(rental)
    }

}