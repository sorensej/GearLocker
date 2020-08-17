package edu.rosehulman.gearlocker.inventory

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import edu.rosehulman.gearlocker.models.ItemCategory
import kotlinx.android.synthetic.main.inventory_card_view_2.view.*
import kotlinx.android.synthetic.main.inventory_sub_item.view.*

class InventoryViewHolder(
    val context: Context,
    itemView: View,
    val inventoryFragment: InventoryAdapter.ItemInterface
) : RecyclerView.ViewHolder(itemView) {

    private val COLLAPSED_ITEM_COUNT = 3

    private var items = arrayListOf<String>()

    private val itemsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_ITEMS)

    private var expanded = false

    fun bind(itemCategory: ItemCategory, isManagment: Boolean) {

        if (isManagment) {
            itemView.delete_btn.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("itemCatagory", itemCategory)
                bundle.putParcelable("inventoryFragment", inventoryFragment)
                inventoryFragment.onNavControllerRequest().navigate(R.id.deleteFragment, bundle)
            }
        }
        itemView.delete_btn.isVisible = isManagment
        itemView.item_type_text.text = itemCategory.name

        items.clear()
        items = itemCategory.items

        itemView.see_more_btn.setOnClickListener {
            expanded = !expanded
            itemView.see_more_btn.text = context.getString(
                if (expanded) {
                    R.string.see_less
                } else {
                    R.string.see_more
                }
            )
            addItems(isManagment)
        }

        addItems(isManagment)

    }

    fun addItems(isManagment: Boolean) {
        itemView.content_container.removeAllViews()

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        for (i in 0 until items.size) {
            if (!expanded && itemView.content_container.childCount >= COLLAPSED_ITEM_COUNT) {
                break
            }
            val child = inflater.inflate(R.layout.inventory_sub_item, null, false)

            itemsRef.document(items[i]).get().addOnSuccessListener { snapshot ->
                val item = Item.fromSnapshot(snapshot)
                if (!item.currentlyRented) {
                    child.sub_item_name.text = item.name
                    child.setOnClickListener {
                        inventoryFragment.onItemSelected(item)
                    }
                    if (isManagment){
                        child.isRented.text = "Available"
                    } else {
                        child.isRented.isVisible = false
                    }
                    if (expanded || itemView.content_container.childCount <= COLLAPSED_ITEM_COUNT) {
                        itemView.content_container.addView(child)
                    }
                } else if (isManagment) {
                    child.isRented.text = "Rented"
                    child.sub_item_name.text = item.name
                    child.setOnClickListener {
                        inventoryFragment.onItemSelected(item)
                    }
                    if (expanded || itemView.content_container.childCount <= COLLAPSED_ITEM_COUNT) {
                        itemView.content_container.addView(child)
                    }
                }
            }
        }

    }
}