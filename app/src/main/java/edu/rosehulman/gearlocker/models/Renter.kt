package edu.rosehulman.gearlocker.models

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

data class Renter (
    var name: String = "",
    var phoneNumber: String = ""
) {
    @get:Exclude var id: String = ""

    companion object {
        fun fromSnapshot(doc: DocumentSnapshot): Renter {
            val renter = doc.toObject(Renter::class.java)!!
            renter.id = doc.id
            return renter
        }
    }
}