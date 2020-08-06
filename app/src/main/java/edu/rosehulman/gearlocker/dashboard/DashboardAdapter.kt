package edu.rosehulman.gearlocker.dashboard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Rental


class DashboardAdapter(private val context: Context
) : RecyclerView.Adapter<DashboardViewHolder>() {

    private val rentals = arrayListOf<Rental>(Rental(), Rental(), Rental())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view: CardView=
            LayoutInflater.from(context).inflate(R.layout.dashboard_card_view, parent, false) as CardView
        return DashboardViewHolder(view)
    }

    override fun getItemCount() = rentals.size

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bind(rentals[position])
    }


}