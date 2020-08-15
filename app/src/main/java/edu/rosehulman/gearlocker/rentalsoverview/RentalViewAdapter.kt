package edu.rosehulman.gearlocker.rentalsoverview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Rental

class RentalViewAdapter(
    val rental: Rental,
    val context: Context?,
    val findNavController: NavController,
    val rentalHandler: RentalRequestViewHolder.RentalHandler?
) : RecyclerView.Adapter<RentalViewViewHolder>() {

    var items = ArrayList<String>()

    init {
        items = rental.itemList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentalViewViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.check_in_cardview,
            parent,
            false
        )
        return RentalViewViewHolder(view)
    }

    override fun getItemCount()=items.size

    override fun onBindViewHolder(holder: RentalViewViewHolder, position: Int) {
        holder.bind(items[position], findNavController, rentalHandler, rental)
    }

}