package edu.rosehulman.gearlocker.deleteitem

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.inventory.InventoryAdapter


class DeleteAdapter(
    private val context: Context,
    private val items: ArrayList<String>,
   private val inventoryFragment: InventoryAdapter.ItemInterface?
) : RecyclerView.Adapter<DeleteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.delete_card_view, parent, false)
        return DeleteViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: DeleteViewHolder, position: Int) {
        holder.bind(items[position], inventoryFragment as InventoryAdapter.ItemInterface, this)
    }


}