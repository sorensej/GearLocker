package edu.rosehulman.gearlocker.rentalsoverview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Rental
import java.util.*



class CurrentRentalsAdapter(
    val context: Context?,
    val findNavController: NavController
) : RecyclerView.Adapter<CurrentRentalsViewHolder>() {

    var currentRentals = ArrayList<Rental>()

    private val currentRentalsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_RENTALS)

    init {
        currentRentalsRef.addSnapshotListener { snapshot, exception ->
            handleSnapshotEvent(snapshot, exception)
        }
    }

    private fun handleSnapshotEvent(
        snapshot: QuerySnapshot?,
        exception: FirebaseFirestoreException?
    ) {
        if (exception != null) {
            Log.e(Constants.TAG, "Dashboard Listen Error: $exception")
            return
        }

        for (change in snapshot!!.documentChanges) {
            val rental = Rental.fromSnapshot(change.document)

            if (rental.confirmed) {
                when (change.type) {
                    DocumentChange.Type.ADDED    -> {
                        currentRentals.add(0, rental)
                        notifyItemInserted(0)
                    }
                    DocumentChange.Type.REMOVED  -> {
                        val position = currentRentals.indexOfFirst { it.id == rental.id }
                        currentRentals.removeAt(position)
                        notifyItemRemoved(position)
                    }
                    DocumentChange.Type.MODIFIED -> {
                        val position = currentRentals.indexOfFirst { it.id == rental.id }
                        currentRentals[position] = rental
                        notifyItemChanged(position)
                    }
                }
            }

        }
    }

    fun add(rental: Rental){
        rental.confirmed = true
        currentRentalsRef.add(rental)
    }


    override fun getItemCount(): Int = currentRentals.size


    override fun onBindViewHolder(holder: CurrentRentalsViewHolder, position: Int) {
        holder.bind(currentRentals[position], findNavController)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentRentalsViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.inventory_sub_item,
            parent,
            false
        )
        return CurrentRentalsViewHolder(view)
    }
}