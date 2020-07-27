package edu.rosehulman.gearlocker.inventory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item

class InventoryAdapter(
    private val context: Context
) : RecyclerView.Adapter<InventoryViewHolder>() {

    private val inventory = arrayListOf<Item>(
        Item(
            "Item #1",
            10f,
            4
        ),
        Item(
            "Item #1",
            10f,
            4
        ),
        Item(
            "Item #1",
            10f,
            4
        ),
        Item(
            "Item #1",
            10f,
            4
        )
    )

    init {
        //currentRentals.add()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.inventory_card_view, parent, false)
        return InventoryViewHolder(view)
    }

    override fun getItemCount() = inventory.size

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        holder.bind(inventory[position])
    }
}