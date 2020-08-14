package edu.rosehulman.gearlocker.deleteitem

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.inventory.InventoryAdapter
import edu.rosehulman.gearlocker.models.Item


class DeleteAdapter(
    private val context: Context,
    private val items: ArrayList<String>,
    private val inventoryFragment: InventoryAdapter.ItemInterface?,
    private val view: View
) : RecyclerView.Adapter<DeleteViewHolder>() {

    private var itemList = ArrayList<String>()
    private val itemDeletedRef = FirebaseFirestore.getInstance().collection(Constants.FB_DELETED)

    init {
        itemList = items
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.delete_card_view, parent, false)
        return DeleteViewHolder(view)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: DeleteViewHolder, position: Int) {
        holder.bind(itemList[position], this)
    }

    fun removeAt(adapterPosition: Int, item: Item) {
        itemList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
        val snackbar: Snackbar =
            Snackbar.make(view, "${item.name} Deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO") {
                    add(item, adapterPosition)
                }
        snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                (inventoryFragment as InventoryAdapter.ItemInterface).onGetAdapter().delete(item)
            }
        })

        snackbar.show()
    }

    fun add(item: Item, adapterPosition: Int) {
        Log.d(Constants.TAG, "$adapterPosition")
        itemList.add(adapterPosition, item.id)
        itemDeletedRef.add(item)
        notifyItemInserted(adapterPosition)

    }


}