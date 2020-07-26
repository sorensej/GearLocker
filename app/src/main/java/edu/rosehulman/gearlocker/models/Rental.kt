package edu.rosehulman.gearlocker.models

import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

data class Rental (
    var startDate: Date = Date.from(Instant.EPOCH),
    var endDate: Date = Date.from(Instant.EPOCH),
    var itemList: ArrayList<Item> = ArrayList()
)