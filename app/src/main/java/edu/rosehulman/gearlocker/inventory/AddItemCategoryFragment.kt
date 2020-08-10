package edu.rosehulman.gearlocker.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.ItemCategory
import kotlinx.android.synthetic.main.dialog_add_item_category.view.*

class AddItemCategoryFragment : Fragment() {

    private val inventoryCategoryRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_ITEM_CATEGORIES)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_item_category, container, false)

        view.add_category_button.setOnClickListener {
            val newItemCategory = ItemCategory(view.category_edit_text.text.toString())
            inventoryCategoryRef.add(newItemCategory).addOnSuccessListener {
                findNavController().navigate(R.id.navigation_management_inventory)
            }
        }

        return view
    }
}