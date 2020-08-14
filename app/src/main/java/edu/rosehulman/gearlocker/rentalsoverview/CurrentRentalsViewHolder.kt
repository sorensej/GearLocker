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

    fun bind(rental: Rental, findNavController: NavController){
        rentalName.text = "${rental.id}: ${rental.startDate} to ${rental.endDate}"
        rentalName.setOnClickListener {
            var bundle: Bundle = Bundle()
            bundle.putParcelable("rental", rental)
            findNavController.navigate(R.id.rentalView, bundle)
        }
    }

}