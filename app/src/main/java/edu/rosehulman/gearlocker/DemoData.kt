package edu.rosehulman.gearlocker

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.models.Club
import edu.rosehulman.gearlocker.models.Item
import edu.rosehulman.gearlocker.models.Rental
import edu.rosehulman.gearlocker.models.Renter
import java.text.SimpleDateFormat
import java.util.*

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

    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)

    val RENTALS = arrayListOf<Rental>(
        Rental(
            dateFormat.parse("07/07/2020")!!,
            dateFormat.parse("07/14/2020")!!,
            arrayListOf(ITEMS[0])
        ),
        Rental(dateFormat.parse("07/15/2020")!!,
            dateFormat.parse("07/17/2020")!!,
            arrayListOf(ITEMS[0])
        ),
        Rental(
            dateFormat.parse("07/07/2020")!!,
            dateFormat.parse("07/17/2020")!!,
            arrayListOf(ITEMS[1])
        ),
        Rental(
            dateFormat.parse("06/23/2020")!!,
            dateFormat.parse("06/26/2020")!!,
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

    fun createRentals() {
        Log.d(Constants.TAG, "Adding rentals")
        val currentRentalsRef = FirebaseFirestore
            .getInstance()
            .collection(Constants.FB_RENTALS)

        for (rental in RENTALS) {
            Log.d(Constants.TAG, rental.startDate.toString())
            currentRentalsRef.add(rental);
        }
    }
}