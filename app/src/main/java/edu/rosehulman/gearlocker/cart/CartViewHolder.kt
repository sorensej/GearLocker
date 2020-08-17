package edu.rosehulman.gearlocker.cart

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.synthetic.main.rental_item_cardview.view.*


class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val title = itemView.rentalitem_text
    private val cost = itemView.cost_text


    fun bind(item: Item) {
        title.text = item.name
        cost.text = String.format("$%.2f", item.estimatedCost)
    }

}