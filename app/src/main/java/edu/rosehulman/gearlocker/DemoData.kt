package edu.rosehulman.gearlocker

import edu.rosehulman.gearlocker.models.Club
import edu.rosehulman.gearlocker.models.Item
import edu.rosehulman.gearlocker.models.Rental
import edu.rosehulman.gearlocker.models.Renter
import java.text.DateFormat

object DemoData {
    val CLUBS = arrayListOf<Club>(
        Club("Climbing Club"),
        Club("Cross Country Team")
    )

    val ITEMS = arrayListOf<Item>(
        Item("Nike Vaporfly", 100.0f, 5, ""),
        Item("Brooks Ravenna", 60.0f, 3, ""),
        Item("La Sportiva Tarantulaces", 95.0f, 5, ""),
        Item("La Sportiva Tarantulaces", 55.0f, 3, ""),
        Item("La Sportiva Tarantulaces", 20.0f, 2, ""),
        Item("La Sportiva Tarantulaces", 95.0f, 5, "")
    )

    private val dateFormat = DateFormat.getDateInstance()

    val RENTALS = arrayListOf<Rental>(
        Rental(
            dateFormat.parse("2020-07-07")!!,
            dateFormat.parse("2020-07-14")!!,
            arrayListOf(ITEMS[0])
        ),
        Rental(dateFormat.parse("2020-07-15")!!,
            dateFormat.parse("2020-07-17")!!,
            arrayListOf(ITEMS[0])
        ),
        Rental(
            dateFormat.parse("2020-07-07")!!,
            dateFormat.parse("2020-07-17")!!,
            arrayListOf(ITEMS[1])
        ),
        Rental(
            dateFormat.parse("2020-06-23")!!,
            dateFormat.parse("2020-06-26")!!,
            arrayListOf(
                ITEMS[2],
                ITEMS[5],
                ITEMS[3]
            )
        )
    )

    val RENTERS = arrayListOf<Renter>(
        Renter("Renter #1", "000-000-0000"),
        Renter("Renter #2", "111-111-1111")
    )
}