package edu.rosehulman.gearlocker.dashboard

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import edu.rosehulman.gearlocker.models.Rental


class DashboardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val title = itemView.findViewById<TextView>(R.id.dashboard_title_item_text)
    private val list = itemView.findViewById<TextView>(R.id.item_list)

    @SuppressLint("SetTextI18n")
    fun bind(rental: Rental){

        val stringBuilder = StringBuilder()

        for ( item: Item in rental.itemList){
            stringBuilder.append(item.name+"\n")
        }

        title.text = "Rental from ${rental.startDate} to ${rental.endDate}:"
        list.text = stringBuilder.toString()

    }

}