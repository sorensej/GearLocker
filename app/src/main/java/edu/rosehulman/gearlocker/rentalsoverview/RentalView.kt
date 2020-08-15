package edu.rosehulman.gearlocker.rentalsoverview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.gearlocker.R
import kotlinx.android.synthetic.main.management_activity_main.*
import kotlinx.android.synthetic.main.rental_overview.view.*

class RentalView : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args : RentalViewArgs by navArgs()
        val constraintView =
            inflater.inflate(
                R.layout.rental_overview,
                container,
                false
            ) as ConstraintLayout
        activity?.nav_host_fragment_management?.findNavController()?.graph?.startDestination =
            R.id.rentalView
        constraintView.items_recyclerview.adapter = RentalViewAdapter(args.rental!!, context, findNavController(), args.rentalHandler)
        constraintView.items_recyclerview.layoutManager = LinearLayoutManager(context)
        return constraintView
    }
}