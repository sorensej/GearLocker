package edu.rosehulman.gearlocker.deleteitem

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Color.*
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.collection.LLRBNode
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.synthetic.main.delete_card_view.view.*


class DeleteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val title = itemView.delete_item_title
    private val condition = itemView.condition_text
    private val cost = itemView.price_text
    private val status = itemView.status_text

    private val itemsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_ITEMS)

    @SuppressLint("ResourceAsColor")
    fun bind(itemString: String){
        itemsRef.document(itemString).get().addOnSuccessListener { snapshot ->
            val item = Item.fromSnapshot(snapshot)
            title.text = item.name

            condition.text = item.condition.toString()
            cost.text = item.estimatedCost.toString()
        }
    }

}