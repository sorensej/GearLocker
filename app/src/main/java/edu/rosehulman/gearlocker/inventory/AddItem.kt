package edu.rosehulman.gearlocker.inventory

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.synthetic.main.add_edit_management.view.*
import kotlinx.android.synthetic.main.add_item_confirmation.view.*

class AddItem: Fragment() {

    private val itemCategoriesRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_ITEM_CATEGORIES)

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.add_edit_management, container, false)
        Log.d(Constants.TAG, "View created")
        activity?.findNavController(R.id.nav_host_fragment_management)?.graph?.startDestination = R.id.addItem
        Log.d(Constants.TAG, "Set fragment")
        val args: AddItemArgs by navArgs()
        view.submit_button.setOnClickListener {
            var item = Item()
                        item.name = view.name_of_gear_edittext.text.toString()
            item.estimatedCost = view.quantity_edittext.text.toString().toFloat()
            item.condition = view.seekBar.progress
            item.description = view.description_edittext.text.toString()
            item.category = view.category_spinner.selectedItem.toString()
            (args.inventoryFragment as InventoryAdapter.ItemInterface).onItemAdded(item)
            Log.d(Constants.TAG, "In AddItem submitted")
            var alertView = LayoutInflater.from(activity).inflate(R.layout.add_item_confirmation, null)
            Log.d(Constants.TAG, "View alert created")
            var builderCreated = AlertDialog.Builder(activity).create()
            alertView.view_inventory.setOnClickListener {
                findNavController().navigate(R.id.navigation_management_inventory)
                builderCreated.dismiss()
            }
            alertView.add_additional_item.setOnClickListener {
                findNavController().navigate(R.id.addItem)
                builderCreated.dismiss()
            }
            builderCreated.setView(alertView)
            builderCreated.show()
        }

        itemCategoriesRef.get().addOnSuccessListener { snapshot ->
            val categories = snapshot.documents.map { it.get("name") as String }.sorted()
            view.category_spinner.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        }

        return view
    }
}