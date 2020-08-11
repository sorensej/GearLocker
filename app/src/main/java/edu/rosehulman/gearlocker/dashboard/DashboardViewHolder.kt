package edu.rosehulman.gearlocker.dashboard

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Rental


class DashboardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    private val itemsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_ITEMS)

    private val title = itemView.findViewById<TextView>(R.id.dashboard_title_item_text)
    private val list = itemView.findViewById<TextView>(R.id.item_list)

    @SuppressLint("SetTextI18n")
    fun bind(rental: Rental){

        val stringBuilder = StringBuilder()

        for (item: String in rental.itemList) {
            itemsRef.document(item).get().addOnSuccessListener { snapshot ->
                stringBuilder.append(snapshot.getString("name") + "\n")
                list.text = stringBuilder.toString()
            }
        }

        title.text = "Rental from ${rental.startDate} to ${rental.endDate}:"
        list.text = stringBuilder.toString()
    }

}