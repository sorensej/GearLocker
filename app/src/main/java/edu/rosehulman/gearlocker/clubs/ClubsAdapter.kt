package edu.rosehulman.gearlocker.clubs

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.models.Club


class ClubsAdapter(private val context: Context
) : BaseAdapter(), Filterable{
    var clubs: ArrayList<Club> = ArrayList<Club>()

    private val currentClubsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_CLUBS)

    init {
        currentClubsRef.addSnapshotListener { snapshot, exception ->
            handleSnapshotEvent(snapshot, exception)
        }
    }

    private fun handleSnapshotEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
        if (exception != null) {
            Log.e(Constants.TAG, "Club Listen Error: $exception")
            return
        }

        for (change in snapshot!!.documentChanges) {
            val club = Club.fromSnapshot(change.document)

            when (change.type) {
                DocumentChange.Type.ADDED -> {
                    clubs.add(0, club)
                    //notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED -> {
                    val position = clubs.indexOfFirst { it.id == club.id }
                    clubs.removeAt(position)
                    //notifyItemRemoved(position)
                }
                DocumentChange.Type.MODIFIED -> {
                    val position= clubs.indexOfFirst { it.id == club.id }
                    clubs[position] = club
                    //notifyItemChanged(position)
                }
            }
        }
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubsViewHolder {
//        val view: CardView=
//            LayoutInflater.from(context).inflate(R.layout.club_cardview, parent, false) as CardView
//        return ClubsViewHolder(view, context)
//    }
//
//    override fun getItemCount(): Int= clubs.size
//
//    override fun onBindViewHolder(holder: ClubsViewHolder, position: Int) {
//        holder.bind(clubs[position])
//    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int = clubs.size



}