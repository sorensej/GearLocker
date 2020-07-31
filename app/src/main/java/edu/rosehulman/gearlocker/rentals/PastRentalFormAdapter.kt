package edu.rosehulman.gearlocker.rentals

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Rental
import kotlinx.android.synthetic.main.inventory_sub_item.view.*

class PastRentalFormAdapter(val context: Context) : RecyclerView.Adapter<PastRentalFormAdapter.PastRentalFormViewHolder>() {

    private val pastRentals = arrayListOf<Rental>(
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

    inner class PastRentalFormViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(rental: Rental) {
            itemView.sub_item_name.text = rental.startDate.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastRentalFormAdapter.PastRentalFormViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.inventory_sub_item, parent, false)  // TODO: Replace the stand-in layout
        return PastRentalFormViewHolder(view)
    }

    override fun getItemCount() = pastRentals.size

    override fun onBindViewHolder(holder: PastRentalFormViewHolder, position: Int) {
        holder.bind(pastRentals[position])
    }


}