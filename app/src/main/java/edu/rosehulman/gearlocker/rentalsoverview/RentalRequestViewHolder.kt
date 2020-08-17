package edu.rosehulman.gearlocker.rentalsoverview

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
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

    private var itemList = arrayListOf<String>()

    fun bind(
        rental: Rental,
        rentalHandler: RentalHandler
    ) {
        val rentalName: TextView = itemView.sub_item_name
        rentalName.text = "${rental.uid}: ${rental.startDate} to ${rental.endDate}"

        itemList.clear()
        for (item: String in rental.itemList) {
            itemsRef.document(item).get().addOnSuccessListener { snapshot ->
                itemList.add(snapshot.getString("name").toString())
            }
        }

        rentalName.setOnClickListener {
            val builder = AlertDialog.Builder(context)

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

            builder.setMessage(itemList.joinToString("\n"))

            builder.create().show()
        }
    }

    interface RentalHandler : Parcelable {
        fun confirmRental(rental: Rental)
        fun removeItemFromRental(rental: Rental, item: Item)
        fun onGetNavController(): NavController
    }
}