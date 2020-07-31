package edu.rosehulman.gearlocker.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item (
    var name: String = "",
    var estimatedCost: Float = 0.0f,
    var condition: Int = 0,
    var  description: String = ""
) : Parcelable