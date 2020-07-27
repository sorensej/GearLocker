package edu.rosehulman.gearlocker.inventory

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.synthetic.main.inventory_card_view.view.*

class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: Item) {
        itemView.item_type_text.text = item.name
    }
}