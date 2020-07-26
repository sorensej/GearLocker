package edu.rosehulman.gearlocker.inventory

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Rental
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

class InventoryAdapter(
    private val context: Context
) : RecyclerView.Adapter<InventoryViewHolder>() {

    private val currentRentals = arrayListOf<Rental>(
        Rental(Date.from(Instant.EPOCH), Date.from(Instant.now()))
    )

    init {
        //currentRentals.add()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.inventory_card_view, parent, false)
        return InventoryViewHolder(view)
    }

    override fun getItemCount() = currentRentals.size

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        holder.bind(currentRentals[position])
    }
}