package edu.rosehulman.gearlocker.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
class Cart(var arrayList: ArrayList<Item> = ArrayList()) : Parcelable {

    @get:Exclude
    var id: String = ""

    companion object {
        fun fromSnapshot(doc: DocumentSnapshot): Cart{
            val c = doc.toObject(Cart::class.java)!!
            c.id = doc.id
            return c
        }
    }
}