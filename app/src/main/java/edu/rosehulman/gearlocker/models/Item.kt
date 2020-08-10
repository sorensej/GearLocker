package edu.rosehulman.gearlocker.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item (
    var name: String = "",
    var estimatedCost: Float = 0.0f,
    var condition: Int = 0,
    var description: String = "",
    var category: String = ""
) : Parcelable {
    @IgnoredOnParcel
    @get:Exclude var id: String = ""

    companion object {
        fun fromSnapshot(doc: DocumentSnapshot): Item {
            val item = doc.toObject(Item::class.java)!!
            item.id = doc.id
            return item
        }
    }
}