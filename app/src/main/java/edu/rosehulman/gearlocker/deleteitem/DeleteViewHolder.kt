package edu.rosehulman.gearlocker.deleteitem

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item


class DeleteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var title: TextView? = itemView.findViewById(R.id.delete_item_title)
    private var condition: TextView? = itemView.findViewById(R.id.condition_text)
    private var cost: TextView? = itemView.findViewById(R.id.price_text)
    private var delete: Button? = itemView.findViewById(R.id.delete_button)
    private var image: ImageView? = itemView.findViewById(R.id.delete_image)
    private var status: TextView? = itemView.findViewById(R.id.status_text)

    private val itemsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_ITEMS)


    @SuppressLint("ResourceAsColor")
    fun bind(
        itemString: String,
        deleteAdapter: DeleteAdapter
    ) {
        itemsRef.document(itemString).get().addOnSuccessListener { snapshot ->
            val item = Item.fromSnapshot(snapshot)
            Picasso.get().load(item.curPhotoPath).into(image)
            title?.text = item.name
            cost?.text = item.estimatedCost.toString()
            status?.text = if (item.currentlyRented) {
                "Rented"
            } else {
                "Available"
            }
            delete?.setOnClickListener {
                deleteAdapter.removeAt(adapterPosition, item)
            }

            condition?.text = when (item.condition) {
                1    -> "Replace"
                2    -> "Bad"
                3    -> "Average"
                4    -> "Good"
                5    -> "New"
                else -> "No Condition"
            }
        }
    }

}