package edu.rosehulman.gearlocker.dashboard

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.models.Rental
import kotlinx.android.synthetic.main.dashboard_card_view.view.*


class DashboardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val title = itemView.dashboard_title_item_text
    private val list = itemView.item_list

    @SuppressLint("SetTextI18n")
    fun bind(rental: Rental){
        Log.d(Constants.TAG, "binding item")
        val newList = arrayListOf<String>("Item 1, Item 2, Item 3")

        for (item in rental.itemList){
            newList.add(item.name)
        }
        val finalList: List<String> = newList.toList()
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(itemView.context, android.R.layout.simple_list_item_1, finalList)
        title.text = "Rental from ${rental.startDate} to ${rental.endDate}:"
        list.adapter = arrayAdapter
    }

}