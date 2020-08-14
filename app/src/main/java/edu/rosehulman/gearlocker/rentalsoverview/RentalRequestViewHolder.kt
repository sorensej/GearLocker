package edu.rosehulman.gearlocker.rentalsoverview

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.models.Rental
import kotlinx.android.synthetic.main.inventory_sub_item.view.*

class RentalRequestViewHolder(itemView: View, val context: Context?) :
    RecyclerView.ViewHolder(itemView) {

    private val itemsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_ITEMS)

    fun bind(
        rental: Rental,
        rentalHandler: RentalHandler
    ) {
        val rentalName: TextView = itemView.sub_item_name
        rentalName.text = "${rental.uid}: ${rental.startDate} to ${rental.endDate}"
        rentalName.setOnClickListener {
            var builder = AlertDialog.Builder(context)

            val stringBuilder = StringBuilder()

            for (item: String in rental.itemList){
                itemsRef.document(item).get().addOnSuccessListener { snapshot ->
                    stringBuilder.append(snapshot.getString("name") + "\n")
                }
            }

            builder.setTitle("${rental.startDate} to ${rental.endDate} for ${rental.uid}")
            Log.d(Constants.TAG, "StringBuilder: ${stringBuilder.toString()}")
            builder.setMessage(stringBuilder.toString())


            builder.setPositiveButton("Confirm Rental") { dialogInterface: DialogInterface, i: Int ->
                rentalHandler.confirmRental(rental, adapterPosition)
            }

            builder.setNegativeButton("Cancel") { dialog: DialogInterface?, _: Int ->
                dialog?.dismiss()
            }
            builder.create().show()
        }
    }

    interface RentalHandler {
        fun confirmRental(rental: Rental, position: Int)
    }
}