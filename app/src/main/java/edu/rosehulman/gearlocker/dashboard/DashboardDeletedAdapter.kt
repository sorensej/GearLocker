package edu.rosehulman.gearlocker.dashboard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Rental


class DashboardDeletedAdapter(private val context: Context
) : RecyclerView.Adapter<DashboardViewHolder>() {

    private val rentals = ArrayList<Rental>()

    private val pastRentalsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_PAST_RENTALS)

    init {
        rentals.clear()
        pastRentalsRef.addSnapshotListener { snapshot, exception ->
            handleSnapshotEvent(snapshot, exception)
        }
        notifyDataSetChanged()
    }

    private fun handleSnapshotEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
        if (exception != null) {
            Log.e(Constants.TAG, "Dashboard Listen Error: $exception")
            return
        }

        val uid = (context as edu.rosehulman.gearlocker.AuthProvider).getUID()

        for (change in snapshot!!.documentChanges) {
            val rental = Rental.fromSnapshot(change.document)

            if (rental.uid != uid) {
                continue
            }

            when (change.type) {
                DocumentChange.Type.ADDED -> {
                    rentals.add(0, rental)
                    notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
                    val position = rentals.indexOfFirst { it.id == rental.id }
                    rentals.removeAt(position)
                    notifyItemRemoved(position)
                }
                DocumentChange.Type.MODIFIED -> {
                    val position = rentals.indexOfFirst { it.id == rental.id }
                    rentals[position] = rental
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view: CardView=
            LayoutInflater.from(context).inflate(R.layout.dashboard_card_view, parent, false) as CardView
        return DashboardViewHolder(view)
    }

    override fun getItemCount() = rentals.size

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bind(rentals[position])
    }


}