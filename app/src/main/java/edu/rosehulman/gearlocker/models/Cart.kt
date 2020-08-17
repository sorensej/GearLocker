package edu.rosehulman.gearlocker.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
class Cart(var arrayList: ArrayList<Item> = ArrayList(),
           var currentStartDate: Date? = null,
           var currentEndDate: Date? = null
) : Parcelable {

    @get:Exclude
    var id: String = ""

    companion object {
        fun fromSnapshot(doc: DocumentSnapshot): Cart{
            val c = doc.toObject(Cart::class.java)!!
            c.id = doc.id
            return c
        }
    }

    fun toRental(uid: String, startDate: Date, endDate: Date, rentingFrom: String): Rental {
        val itemIds = ArrayList(arrayList.map { it.id })
        return Rental(startDate, endDate, itemIds, uid, rentedFrom = rentingFrom)
    }
}