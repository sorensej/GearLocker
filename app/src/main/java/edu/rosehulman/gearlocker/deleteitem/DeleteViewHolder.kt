package edu.rosehulman.gearlocker.deleteitem

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.models.Item
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