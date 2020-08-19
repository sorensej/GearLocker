package edu.rosehulman.gearlocker.messages

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.AuthProvider
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Message
import kotlinx.android.synthetic.main.message_cardview.view.*
import java.text.SimpleDateFormat


class MessagesViewHolder(itemView: View, private val authProvider: AuthProvider) : RecyclerView.ViewHolder(itemView) {

    private val dateFormat = SimpleDateFormat("MM/dd/yyy hh:mm a")

    @SuppressLint("ResourceType")
    fun bind(message: Message){
        Log.d(Constants.TAG, "${message.toString()}")
        itemView.timestamp_textview.text = dateFormat.format(message.sentTimestamp!!.toDate())
        itemView.message_textview.text = message.message

        val uid = authProvider.getUID()

        if (message.sender != uid){
            itemView.message_textview.background= itemView.context.getDrawable(R.drawable.text_bubble_reciever)
            itemView.layoutDirection = View.LAYOUT_DIRECTION_LTR
            itemView.message_textview.setPadding(60, 15, 15, 15)
        } else {
            Log.d(Constants.TAG, "other side")
            itemView.layoutDirection = View.LAYOUT_DIRECTION_RTL
            var params = itemView.layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = 45

            itemView.message_textview.setPadding(15, 15, 60, 15)
        }
    }
}