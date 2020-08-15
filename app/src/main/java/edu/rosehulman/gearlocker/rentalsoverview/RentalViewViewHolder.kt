package edu.rosehulman.gearlocker.rentalsoverview

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import edu.rosehulman.gearlocker.models.Rental
import kotlinx.android.synthetic.main.check_in_cardview.view.*

class RentalViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val itemsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_ITEMS)

    fun bind (
        item: String,
        findNavController: NavController,
        rentalHandler: RentalRequestViewHolder.RentalHandler?,
        rental: Rental
    ){
        itemsRef.document(item).get().addOnSuccessListener { snapshot ->
            val itemSnapshot = Item.fromSnapshot(snapshot)
            itemView.sub_item_name.text = itemSnapshot.name
            itemView.check_in_button.setOnClickListener {
                var bundle = Bundle()
                bundle.putParcelable("item", itemSnapshot)
                bundle.putParcelable("rentalHandler", rentalHandler)
                bundle.putParcelable("rental", rental)
                findNavController.navigate(R.id.checkInFragment, bundle)
            }
        }
    }
}