package edu.rosehulman.gearlocker.models

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

class Cart(var arrayList: ArrayList<Item> = ArrayList()) {

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