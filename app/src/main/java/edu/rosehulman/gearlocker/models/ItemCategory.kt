package edu.rosehulman.gearlocker.models

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

data class ItemCategory (
    var name: String = "",
    var items: ArrayList<String> = arrayListOf<String>()
) {
    @get:Exclude var id: String = ""

    companion object {
        fun fromSnapshot(doc: DocumentSnapshot): ItemCategory {
            val item = doc.toObject(ItemCategory::class.java)!!
            item.id = doc.id
            return item
        }
    }
}