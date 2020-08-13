package edu.rosehulman.gearlocker.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemCategory (
    var name: String = "",
    var items: ArrayList<String> = arrayListOf<String>()
) : Parcelable {
    @get:Exclude var id: String = ""

    companion object {
        fun fromSnapshot(doc: DocumentSnapshot): ItemCategory {
            val item = doc.toObject(ItemCategory::class.java)!!
            item.id = doc.id
            return item
        }
    }
}