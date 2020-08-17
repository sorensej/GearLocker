package edu.rosehulman.gearlocker.rentalsoverview

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
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
class RentalsOverviewManagment : Fragment(), RentalRequestViewHolder.RentalHandler, Parcelable {

    @IgnoredOnParcel
    private val formsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_FORMS)

    private val rentalsRef = FirebaseFirestore.getInstance().collection(Constants.FB_RENTALS)

    private val pastRentalsRef = FirebaseFirestore.getInstance().collection(Constants.FB_PAST_RENTALS)


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
            inflater.inflate(
                R.layout.fragment_rental_overview,
                container,
                false
            ) as ConstraintLayout
        activity?.nav_host_fragment_management?.findNavController()?.graph?.startDestination =
            R.id.rentalsOverviewManagment
        curRentalsAdapter = CurrentRentalsAdapter(
            context,
            findNavController(),
            this as RentalRequestViewHolder.RentalHandler
        )
        rentalRequestAdapter =
            RentalRequestsAdapter(context, this as RentalRequestViewHolder.RentalHandler)
        constraintView.cur_rentals_recyclerview.adapter = curRentalsAdapter
        constraintView.rental_requests_recyclerview.adapter = rentalRequestAdapter
        constraintView.cur_rentals_recyclerview.layoutManager = LinearLayoutManager(context)
        constraintView.rental_requests_recyclerview.layoutManager = LinearLayoutManager(context)
        return constraintView
    }

    override fun confirmRental(rental: Rental) {
        Log.d(Constants.TAG, "Rental form: ${rental.forms}")
        rentalsRef.document(rental.id).set(rental)
        rentalRequestAdapter?.confirmRental(rental)
        curRentalsAdapter?.add(rental)
    }

    override fun removeItemFromRental(rental: Rental, item: Item) {
        itemsRef.document(item.id).update("currentlyRented", false)
        Log.d(Constants.TAG, "removing item: ${item.id}")
        if (rental.itemList.size != 1) {
            Log.d(Constants.TAG, "size is 1")
            curRentalsAdapter!!.checkIn(item, rental)
        } else {
            Log.d(Constants.TAG, "remove entire rental")
            curRentalsAdapter!!.checkIn(item, rental)
            pastRentalsRef.add(rental)
            curRentalsAdapter!!.remove(rental)
            Log.d(Constants.TAG, rental.forms)
            formsRef.document(rental.forms).update("current", false)

        }
    }

    override fun onGetNavController(): NavController {
        return findNavController()
    }

}