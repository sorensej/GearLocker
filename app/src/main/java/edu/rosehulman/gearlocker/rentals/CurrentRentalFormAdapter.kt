package edu.rosehulman.gearlocker.rentals

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Rental
import kotlinx.android.synthetic.main.inventory_sub_item.view.*

class CurrentRentalFormAdapter(val context: Context) : RecyclerView.Adapter<CurrentRentalFormAdapter.CurrentRentalFormViewHolder>() {

    private val currentRentals = arrayListOf<Rental>(
        Rental(),
        Rental(),
        Rental(),
        Rental(),
        Rental(),
        Rental(),
        Rental(),
        Rental(),
        Rental(),
        Rental(),
        Rental(),
        Rental()
    )

    inner class CurrentRentalFormViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(rental: Rental) {
            itemView.sub_item_name.text = rental.startDate.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentRentalFormViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.inventory_sub_item, parent, false) // TODO: Replace the stand-in layout
        return CurrentRentalFormViewHolder(view)
    }

    override fun getItemCount() = currentRentals.size

    override fun onBindViewHolder(holder: CurrentRentalFormViewHolder, position: Int) {
        holder.bind(currentRentals[position])
    }
}