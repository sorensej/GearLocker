package edu.rosehulman.gearlocker.rentalsoverview

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Rental
import kotlinx.android.synthetic.main.inventory_sub_item.view.*

class CurrentRentalsViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    val rentalName: TextView = itemView.sub_item_name

    fun bind(
        rental: Rental,
        findNavController: NavController,
        rentalHandler: RentalRequestViewHolder.RentalHandler
    ){
        rentalName.text = "${rental.uid}: ${rental.startDate} to ${rental.endDate}"
        rentalName.setOnClickListener {
            var bundle: Bundle = Bundle()
            bundle.putParcelable("rental", rental)
            bundle.putParcelable("rentalHandler", rentalHandler)
            findNavController.navigate(R.id.rentalView, bundle)
        }
    }

}