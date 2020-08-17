package edu.rosehulman.gearlocker.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList
@Parcelize
data class Rental (
 var startDate: Date = Date.from(Instant.EPOCH),
    var endDate: Date = Date.from(Instant.EPOCH),
    var itemList: ArrayList<String> = ArrayList(),
    var uid: String = "",
    var confirmed: Boolean = false,
    var forms: String = ""
) : Parcelable {
    @get:Exclude var id: String = ""

    companion object {
        fun fromSnapshot(doc: DocumentSnapshot): Rental {
            val rental = doc.toObject(Rental::class.java)!!
            rental.id = doc.id
            return rental
        }
    }
}