package edu.rosehulman.gearlocker.models

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

data class Club (
    var name: String = ""
) {
    @get:Exclude var id: String = ""

    companion object {
        fun fromSnapshot(doc: DocumentSnapshot): Club {
            val c = doc.toObject(Club::class.java)!!
            c.id = doc.id
            return c
        }
    }
}