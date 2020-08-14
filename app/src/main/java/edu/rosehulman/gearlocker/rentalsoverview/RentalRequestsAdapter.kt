package edu.rosehulman.gearlocker.rentalsoverview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Rental
import java.util.*

class RentalRequestsAdapter(
    val context: Context?,
    private val rentalHandler: RentalRequestViewHolder.RentalHandler
) : RecyclerView.Adapter<RentalRequestViewHolder>() {

    private var rentalRequests = ArrayList<Rental>()

    private val currentRentalsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_RENTALS)

    init {
        currentRentalsRef.addSnapshotListener { snapshot, exception ->
            handleSnapshotEvent(snapshot, exception)
        }
        notifyDataSetChanged()
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

            if (!rental.confirmed) {
                when (change.type) {
                    DocumentChange.Type.ADDED    -> {
                        Log.d(Constants.TAG, "Rental request added: ${rental}")
                        rentalRequests.add(0, rental)
                        notifyItemInserted(0)
                    }
                    DocumentChange.Type.REMOVED  -> {
                        val position = rentalRequests.indexOfFirst { it.id == rental.id }
                        rentalRequests.removeAt(position)
                        notifyItemRemoved(position)
                    }
                    DocumentChange.Type.MODIFIED -> {
                        val position = rentalRequests.indexOfFirst { it.id == rental.id }
                        rentalRequests[position] = rental
                        notifyItemChanged(position)
                    }
                }
            }

        }
    }

    fun remove(rental: Rental){
        currentRentalsRef.document(rental.id).delete()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentalRequestViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.inventory_sub_item,
            parent,
            false
        )
        return RentalRequestViewHolder(view, context)
    }

    override fun getItemCount() = rentalRequests.size

    override fun onBindViewHolder(holder: RentalRequestViewHolder, position: Int) {
        Log.d(Constants.TAG, "Bind request at position: $position")
        holder.bind(rentalRequests[position], rentalHandler)
    }
}