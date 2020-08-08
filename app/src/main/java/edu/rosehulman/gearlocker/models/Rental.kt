package edu.rosehulman.gearlocker.models

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

data class Rental (
    var startDate: Date = Date.from(Instant.EPOCH),
    var endDate: Date = Date.from(Instant.EPOCH),
    var itemList: ArrayList<Item> = ArrayList(),
    var uid: String = ""
) {
    @get:Exclude var id: String = ""

    companion object {
        fun fromSnapshot(doc: DocumentSnapshot): Rental {
            val rental = doc.toObject(Rental::class.java)!!
            rental.id = doc.id
            return rental
        }
    }
}