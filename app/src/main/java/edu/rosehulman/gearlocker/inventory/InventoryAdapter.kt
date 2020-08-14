package edu.rosehulman.gearlocker.inventory

import android.content.Context
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import edu.rosehulman.gearlocker.models.ItemCategory

class InventoryAdapter(
    private val context: Context,
    private val inventoryFragment: ItemInterface, private val isManagement: Boolean
) : RecyclerView.Adapter<InventoryViewHolder>() {

    private val itemCategories = ArrayList<ItemCategory>()

    private val itemCategoriesRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_ITEM_CATEGORIES)

    private val itemsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_ITEMS)

    init {
        itemCategoriesRef.addSnapshotListener { snapshot, exception ->
            handleSnapshotEvent(snapshot, exception)
        }
        notifyDataSetChanged()
    }

    private fun handleSnapshotEvent(
        snapshot: QuerySnapshot?,
        exception: FirebaseFirestoreException?
    ) {
        if (exception != null) {
            Log.e(Constants.TAG, "Inventory Listen Error: $exception")
            return
        }

        for (change in snapshot!!.documentChanges) {
            val itemCategory = ItemCategory.fromSnapshot(change.document)

            when (change.type) {
                DocumentChange.Type.ADDED    -> {
                    itemCategories.add(itemCategory)
                    notifyItemInserted(0)
                }
                DocumentChange.Type.REMOVED  -> {
                    val position = itemCategories.indexOfFirst { it.id == itemCategory.id }
                    itemCategories.removeAt(position)
                    notifyItemRemoved(position)
                }
                DocumentChange.Type.MODIFIED -> {
                    val position = itemCategories.indexOfFirst { it.id == itemCategory.id }
                    itemCategories[position] = itemCategory
                    notifyItemChanged(position)
                }
            }
        }
    }

    fun add(item: Item) {
        Log.d(Constants.TAG, "$item")
        itemsRef.add(item).addOnSuccessListener { documentRef ->
            itemCategoriesRef.get().addOnSuccessListener { querySnapshot ->
                val doc = querySnapshot.documents.first { it.getString("name") == item.category }
                itemCategoriesRef.document(doc.id)
                    .update("items", FieldValue.arrayUnion(documentRef.id))
            }
        }

    }

    fun delete(item: Item){
        itemCategoriesRef.get().addOnSuccessListener { querySnapshot ->
            val doc = querySnapshot.documents.first { it.getString("name") == item.category }
            itemCategoriesRef.document(doc.id).update("items", FieldValue.arrayRemove(item.id))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.inventory_card_view_2, parent, false)
        return InventoryViewHolder(context, view, inventoryFragment)
    }

    override fun getItemCount() = itemCategories.size

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        holder.bind(itemCategories[position], isManagement)
    }

    interface ItemInterface : Parcelable {
        fun onItemSelected(item: Item)
        fun onItemAdded(item: Item)
        fun onNavControllerRequest(): NavController
        fun onGetAdapter(): InventoryAdapter
    }
}