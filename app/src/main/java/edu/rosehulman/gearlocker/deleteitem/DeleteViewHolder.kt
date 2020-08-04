package edu.rosehulman.gearlocker.deleteitem

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.models.Item
import edu.rosehulman.gearlocker.models.Rental
import kotlinx.android.synthetic.main.dashboard_card_view.view.*
import kotlinx.android.synthetic.main.delete_card_view.view.*


class DeleteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val title = itemView.delete_item_title
    private val condition = itemView.condition_text
    private val cost = itemView.price_text
    private val status = itemView.status_text


    fun bind(item: Item){
        title.text = item.name
        condition.text = item.condition.toString()
        cost.text = item.estimatedCost.toString()
    }

}