package edu.rosehulman.gearlocker.models

import java.time.Instant
import java.util.*

data class Rental (
    var startDate: Date = Date.from(Instant.EPOCH),
    var endDate: Date = Date.from(Instant.EPOCH)
)