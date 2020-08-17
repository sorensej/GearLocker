package edu.rosehulman.gearlocker.rentalsoverview

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
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

            var array: String = ""

            builder.setMessage(array.toString())

            for (item: String in rental.itemList) {
                itemsRef.document(item).get().addOnSuccessListener { snapshot ->
                    array += snapshot.getString("name").toString()
                    Log.d(Constants.TAG, "Message2: ${array.toString()}")
                }
            }
            Log.d(Constants.TAG, "Message: ${array.toString()}")
            builder.setTitle("${rental.startDate} to ${rental.endDate} for ${rental.uid}")

            builder.setPositiveButton("Upload Form") { dialogInterface: DialogInterface, i: Int ->
                val bundle = Bundle()
                bundle.putParcelable("rentalHandler", rentalHandler)
                bundle.putParcelable("rental", rental)
                rentalHandler.onGetNavController().navigate(R.id.formUploadFragment, bundle)
            }

            builder.setNegativeButton("Cancel") { dialog: DialogInterface?, _: Int ->
                dialog?.dismiss()
            }
            builder.create().show()
        }
    }

    interface RentalHandler : Parcelable {
        fun confirmRental(rental: Rental)
        fun removeItemFromRental(rental: Rental, item: Item)
        fun onGetNavController(): NavController
    }
}