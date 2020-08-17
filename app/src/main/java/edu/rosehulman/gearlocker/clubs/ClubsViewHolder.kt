package edu.rosehulman.gearlocker.clubs

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Club
import kotlinx.android.synthetic.main.club_cardview.view.*


class ClubsViewHolder(itemView: View, val context: Context): RecyclerView.ViewHolder(itemView){
    val button: TextView = itemView.add_club_textbutton
    private val clubText: TextView = itemView.club_textview

    private val auth = FirebaseAuth.getInstance().currentUser

    fun bind(club: Club){
        clubText.text = club.name

        if (club.members.containsValue(auth?.uid)) {
            button.text = context.getString(R.string.leave_club)
            button.setBackgroundResource(R.drawable.round_button_red)
            button.setOnClickListener {
                removeFromClub(club)
            }
        } else {
            button.text = context.getString(R.string.add_club)
            button.setBackgroundResource(R.drawable.round_button)
            button.setOnClickListener {
                addToClub(club)
            }
        }
    }

    private fun addToClub(club: Club) {
        if (auth != null) {
            val displayName = if (!auth.displayName.isNullOrEmpty()) { auth.displayName!! } else { auth.uid }
            club.members.putIfAbsent(displayName, auth.uid)
            Log.d(Constants.TAG, club.members.toString())
            FirebaseFirestore
                .getInstance()
                .collection(Constants.FB_CLUBS)
                .document(club.id)
                .set(club)


        }
    }

    private fun removeFromClub(club: Club) {
        if (auth != null) {
            val displayName = if(!auth.displayName.isNullOrEmpty()) { auth.displayName!! } else { auth.uid }

            if (!club.members.containsKey(displayName)) {
                return
            }

            club.members.remove(displayName)
            Log.d(Constants.TAG, club.members.toString())
            FirebaseFirestore
                .getInstance()
                .collection(Constants.FB_CLUBS)
                .document(club.id)
                .set(club)
        }
    }

}