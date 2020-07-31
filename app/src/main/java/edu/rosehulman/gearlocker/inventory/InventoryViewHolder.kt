package edu.rosehulman.gearlocker.inventory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.synthetic.main.inventory_card_view.view.item_type_text
import kotlinx.android.synthetic.main.inventory_card_view_2.view.*
import kotlinx.android.synthetic.main.inventory_sub_item.view.*

class InventoryViewHolder(
    val context: Context,
    itemView: View,
    val inventoryFragment: InventoryAdapter.ItemInterface
) : RecyclerView.ViewHolder(itemView) {

    private val items = arrayListOf(
        Item(
            "Sub Item #1"
        ),
        Item(
            "Sub Item #2"
        ),
        Item(
            "Sub Item #3"
        ),
        Item(
            "Sub Item #4"
        )
    )

    init{
        itemView.setOnClickListener {
            inventoryFragment.onItemSelected(adapterPosition)
        }
    }

    private var expanded = false

    fun bind(item: Item) {
        itemView.item_type_text.text = item.name

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

        for (i in 0 until if (expanded) { items.size} else { 3 }) {
            val child = inflater.inflate(R.layout.inventory_sub_item, null, false)
            child.sub_item_name.text = items[i].name
            itemView.content_container.addView(child)
        }

    }
}