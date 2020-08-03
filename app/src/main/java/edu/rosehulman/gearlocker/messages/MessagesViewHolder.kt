package edu.rosehulman.gearlocker.messages

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.models.Message
import kotlinx.android.synthetic.main.message_cardview.view.*

class MessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(message: Message){
        itemView.message_textview.text = message.message
    }
}