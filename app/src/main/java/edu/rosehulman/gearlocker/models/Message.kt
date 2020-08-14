package edu.rosehulman.gearlocker.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class Message(
    var sender: String = "",
    var message: String = "",
    var receiver: String = "") {

    @get:Exclude
    var id: String = ""
    @ServerTimestamp var sentTimestamp: Timestamp? = null

    companion object {

        const val SENT_TIMESTAMP_KEY = "sentTimestamp"

        fun fromSnapshot(doc: DocumentSnapshot): Message {
            val message = doc.toObject(Message::class.java)!!
            message.id = doc.id
            return message
        }
    }
}