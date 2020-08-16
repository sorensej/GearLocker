package edu.rosehulman.gearlocker.rentalsoverview

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import edu.rosehulman.gearlocker.models.Rental
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_rental_overview.view.*
import kotlinx.android.synthetic.main.management_activity_main.*
@Parcelize
class RentalsOverviewManagment: Fragment(), RentalRequestViewHolder.RentalHandler, Parcelable {

    @IgnoredOnParcel
    var curRentalsAdapter: CurrentRentalsAdapter? = null
    @IgnoredOnParcel
    var rentalRequestAdapter: RentalRequestsAdapter? = null

    private val itemsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_ITEMS)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val constraintView =
            inflater.inflate(R.layout.fragment_rental_overview, container, false) as ConstraintLayout
        activity?.nav_host_fragment_management?.findNavController()?.graph?.startDestination = R.id.rentalsOverviewManagment
        curRentalsAdapter = CurrentRentalsAdapter(context, findNavController(), this as RentalRequestViewHolder.RentalHandler)
        rentalRequestAdapter =
            RentalRequestsAdapter(context, this as RentalRequestViewHolder.RentalHandler)
        constraintView.cur_rentals_recyclerview.adapter = curRentalsAdapter
        constraintView.rental_requests_recyclerview.adapter = rentalRequestAdapter
        constraintView.cur_rentals_recyclerview.layoutManager = LinearLayoutManager(context)
        constraintView.rental_requests_recyclerview.layoutManager = LinearLayoutManager(context)
        return constraintView
    }

    override fun confirmRental(rental: Rental, position: Int, int: Int) {
        curRentalsAdapter?.add(rental)
        rentalRequestAdapter?.remove(rental)
    }

    override fun removeItemFromRental(rental: Rental, item: Item) {
        itemsRef.document(item.id).update("currentlyRented", false)
        Log.d(Constants.TAG, "removing item: ${item.id}")
        if (rental.itemList.size != 1) {
            Log.d(Constants.TAG, "size is 1")
            curRentalsAdapter!!.checkIn(item, rental)
        }else{
            Log.d(Constants.TAG, "remove entire rental")
            curRentalsAdapter!!.remove(rental)
            rentalRequestAdapter!!.remove(rental)
        }
    }

}