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
    private var title: TextView? = null
    private var condition: TextView? = null
    private var cost: TextView? = null
    private var delete: Button? = null
    private var image: ImageView? = null

    private val itemsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_ITEMS)

    init {
        condition = itemView.findViewById(R.id.condition_text)
        title = itemView.findViewById(R.id.delete_item_title)
        cost = itemView.findViewById(R.id.price_text)
        delete = itemView.findViewById(R.id.delete_button)
    }


    @SuppressLint("ResourceAsColor")
    fun bind(
        itemString: String,
        deleteAdapter: DeleteAdapter
    ) {
        itemsRef.document(itemString).get().addOnSuccessListener { snapshot ->
            val item = Item.fromSnapshot(snapshot)
            Picasso.get().load(item.curPhotoPath).into(image)
            title?.text = item.name
            condition?.text = item.condition.toString()
            cost?.text = item.estimatedCost.toString()
            delete?.setOnClickListener {
                deleteAdapter.removeAt(adapterPosition, item)
            }

//            condition?.setBackgroundResource(when (item.condition){
//                1 -> context.getColor(R.color.colorRed)
//                2-> context.getColor(R.color.colorAccentTwo)
//                3-> {
//                    context.getColor(R.color.colorAccent)
//                    Log.d(Constants.TAG, "Condition is 3")
//                }
//                4-> context.getColor(R.color.colorPrimary)
//                5-> context.getColor(R.color.colorPrimaryDark)
//                else -> android.R.color.white
//            }
//            )
        }
    }

}