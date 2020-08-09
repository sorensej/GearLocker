package edu.rosehulman.gearlocker.inventory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import edu.rosehulman.gearlocker.models.ItemCategory
import kotlinx.android.synthetic.main.inventory_card_view.view.item_type_text
import kotlinx.android.synthetic.main.inventory_card_view_2.view.*
import kotlinx.android.synthetic.main.inventory_sub_item.view.*

class InventoryViewHolder(
    val context: Context,
    itemView: View,
    val inventoryFragment: InventoryAdapter.ItemInterface
) : RecyclerView.ViewHolder(itemView) {

    private var items = arrayListOf<Item>()

    private var expanded = false

    fun bind(itemCategory: ItemCategory) {
        itemView.item_type_text.text = itemCategory.name

        items.clear()
        items = itemCategory.items

        itemView.see_more_btn.setOnClickListener {
            expanded = !expanded
            itemView.see_more_btn.text = context.getString(if (expanded) { R.string.see_less } else { R.string.see_more })
            addItems()
        }

        addItems()

    }

    fun addItems() {
        itemView.content_container.removeAllViews()

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        for (i in 0 until if (expanded) { items.size } else { 3 }) {
            if (i >= items.size) {
                break
            }
            val child = inflater.inflate(R.layout.inventory_sub_item, null, false)
            child.sub_item_name.text = items[i].name
            child.setOnClickListener {
                inventoryFragment.onItemSelected(items[i])
            }
            itemView.content_container.addView(child)
        }

    }
}