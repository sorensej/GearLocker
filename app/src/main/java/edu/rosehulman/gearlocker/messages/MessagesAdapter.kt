package edu.rosehulman.gearlocker.messages


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Message


class MessagesAdapter(private val context: Context) : RecyclerView.Adapter<MessagesViewHolder>() {
    private var messages = arrayListOf(Message("Sender", "This is a message"), Message("Sender", "This is another message and it is a bit longer to see what it will look like."))

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.message_cardview, parent, false)
        return MessagesViewHolder(view)
    }

    override fun getItemCount()=messages.size

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        holder.bind(messages[position])
    }
}