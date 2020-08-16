package edu.rosehulman.gearlocker.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Form(
    var startDate: Date = Date.from(Instant.EPOCH),
    var endDate: Date = Date.from(Instant.EPOCH),
    var itemList: ArrayList<String> = ArrayList(),
    var uid: String = ""
) : Parcelable {
    @get:Exclude
    var id: String = ""

    companion object {
        fun fromSnapshot(doc: DocumentSnapshot): Form {
            val item = doc.toObject(Form::class.java)!!
            item.id = doc.id
            return item
        }
    }
}