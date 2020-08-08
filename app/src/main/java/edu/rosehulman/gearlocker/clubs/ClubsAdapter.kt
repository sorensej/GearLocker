package edu.rosehulman.gearlocker.clubs

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.dashboard.DashboardViewHolder
import edu.rosehulman.gearlocker.models.Club
import edu.rosehulman.gearlocker.models.Rental


class ClubsAdapter(private val context: Context
) : RecyclerView.Adapter<ClubsViewHolder>() {
    private var clubs: ArrayList<Club> = ArrayList<Club>()

    private val currentClubsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_CLUBS)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubsViewHolder {
        val view: CardView=
            LayoutInflater.from(context).inflate(R.layout.club_cardview, parent, false) as CardView
        return ClubsViewHolder(view)
    }

    override fun getItemCount(): Int= clubs.size

    override fun onBindViewHolder(holder: ClubsViewHolder, position: Int) {
        holder.bind(clubs[position])
    }


}