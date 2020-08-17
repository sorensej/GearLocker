package edu.rosehulman.gearlocker.messages


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import edu.rosehulman.gearlocker.AuthProvider
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Message


class MessagesAdapter(private val context: Context, private val sender: String, private val receiver: String) : RecyclerView.Adapter<MessagesViewHolder>() {

    private val messages = ArrayList<Message>()

    private val messagesRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_MESSAGES)

    init {
        messagesRef
            .orderBy(Message.SENT_TIMESTAMP_KEY, Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                handleSnapshotEvent(snapshot, exception)
        }
    }

    private fun handleSnapshotEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
        if (exception != null) {
            Log.e(Constants.TAG, "Messages Listen Error: $exception")
            return
        }

        for (change in snapshot!!.documentChanges) {
            val message = Message.fromSnapshot(change.document)

            if (message.sender != receiver && message.sender != sender) {
                continue
            }

            if (message.receiver != sender && message.receiver != receiver) {
                continue
            }

            when (change.type) {
                DocumentChange.Type.ADDED -> {
                    messages.add(message)
                    notifyItemInserted(messages.size - 1)
                }
                DocumentChange.Type.REMOVED -> {
                    val position = messages.indexOfFirst { it.id == message.id }
                    messages.removeAt(position)
                    notifyItemRemoved(position)
                }
                else -> {}
            }
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.message_cardview, parent, false)
        return MessagesViewHolder(view, context as AuthProvider)
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        holder.bind(messages[position])
    }
}