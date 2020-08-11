package edu.rosehulman.gearlocker

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.models.*
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
        Item("La Sportiva Tarantulaces", 95.0f, 5, ""),
        Item("Petzl Corax", 40.0f, 5, ""),
        Item("Rock & Lock", 20.0f, 1, "")
    )

    val ITEM_CATEGORIES = arrayListOf<ItemCategory>(
        ItemCategory("Shoes", arrayListOf<String>(
            ITEMS[0].id,
            ITEMS[1].id,
            ITEMS[2].id,
            ITEMS[3].id,
            ITEMS[4].id,
            ITEMS[5].id
        )),
        ItemCategory("Harnesses", arrayListOf<String>(
            ITEMS[6].id,
            ITEMS[7].id
        ))
    )

    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)

    val RENTALS = arrayListOf<Rental>(
        Rental(
            dateFormat.parse("07/07/2020")!!,
            dateFormat.parse("07/14/2020")!!,
            arrayListOf(ITEMS[0].id)
        ),
        Rental(dateFormat.parse("07/15/2020")!!,
            dateFormat.parse("07/17/2020")!!,
            arrayListOf(ITEMS[0].id)
        ),
        Rental(
            dateFormat.parse("07/07/2020")!!,
            dateFormat.parse("07/17/2020")!!,
            arrayListOf(ITEMS[1].id)
        ),
        Rental(
            dateFormat.parse("06/23/2020")!!,
            dateFormat.parse("06/26/2020")!!,
            arrayListOf(
                ITEMS[2].id,
                ITEMS[5].id,
                ITEMS[3].id
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

    fun createItemCategories() {
        Log.d(Constants.TAG, "Adding Item Categories")
        val itemCategoriesRef = FirebaseFirestore
            .getInstance()
            .collection(Constants.FB_ITEMS)

        for (itemCategory in ITEM_CATEGORIES) {
            Log.d(Constants.TAG, itemCategory.name)
            itemCategoriesRef.add(itemCategory)
        }
    }

    fun createClubs() {
        Log.d(Constants.TAG, "Adding clubs")
        val clubsRef = FirebaseFirestore
            .getInstance()
            .collection(Constants.FB_CLUBS)

        for (club in CLUBS) {
            Log.d(Constants.TAG, club.name)
            clubsRef.add(club)
        }
    }
}