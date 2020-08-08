package edu.rosehulman.gearlocker.clubs

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Club
import edu.rosehulman.gearlocker.models.Item
import edu.rosehulman.gearlocker.models.Rental
import kotlinx.android.synthetic.main.club_cardview.view.*


class ClubsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val button: TextView = itemView.add_club_textbutton
    private val clubText: TextView = itemView.club_textview

    init{
        button.setOnClickListener {
            Log.d(Constants.TAG, "Clicked")
        }
    }

    fun bind(club: Club){
        clubText.text = club.name
    }

}