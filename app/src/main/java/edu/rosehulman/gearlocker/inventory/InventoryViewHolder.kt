package edu.rosehulman.gearlocker.inventory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.models.Rental
import kotlinx.android.synthetic.main.dashboard_card_view.view.*

class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(rental: Rental) {
        itemView.dashboard_title_item_text.text = rental.startDate.toString()
    }
}