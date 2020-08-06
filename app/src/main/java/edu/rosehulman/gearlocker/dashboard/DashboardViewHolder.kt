package edu.rosehulman.gearlocker.dashboard

import android.annotation.SuppressLint

import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.DemoData
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import edu.rosehulman.gearlocker.models.Rental
import kotlinx.android.synthetic.main.dashboard_card_view.view.*
import java.lang.StringBuilder


class DashboardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val title = itemView.findViewById<TextView>(R.id.dashboard_title_item_text)
    private val list = itemView.findViewById<TextView>(R.id.item_list)

    @SuppressLint("SetTextI18n")
    fun bind(rental: Rental){

        val items = arrayListOf<Item>(
            Item("Nike Vaporfly", 100.0f, 5, ""),
            Item("Brooks Ravenna", 60.0f, 3, ""),
            Item("La Sportiva Tarantulaces", 95.0f, 5, ""),
            Item("La Sportiva Tarantulaces", 55.0f, 3, ""),
            Item("La Sportiva Tarantulaces", 20.0f, 2, ""),
            Item("La Sportiva Tarantulaces", 95.0f, 5, "")
        )

        val stringBuilder = StringBuilder()

        for ( item: Item in items){
            stringBuilder.append(item.name+"\n")
        }

        title.text = "Rental from ${rental.startDate} to ${rental.endDate}:"
        list.text = stringBuilder.toString()

    }

}