package edu.rosehulman.gearlocker.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize
import kotlin.random.Random

data class Club (
    var name: String = "",
    var admins: ArrayList<String> = arrayListOf(),
    var members: MutableMap<String, String> = mutableMapOf(),
    var accessCode: String = Random.nextInt(99999).toString()
) {
    @get:Exclude var id: String = ""

    fun isAdmin(uid: String, passcode: String): Boolean {
        return this.admins.contains(uid) && this.accessCode == passcode
    }

    companion object {
        fun fromSnapshot(doc: DocumentSnapshot): Club {
            val c = doc.toObject(Club::class.java)!!
            c.id = doc.id
            return c
        }
    }
}