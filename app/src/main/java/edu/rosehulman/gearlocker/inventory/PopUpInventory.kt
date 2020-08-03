package edu.rosehulman.gearlocker.inventory

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.synthetic.main.gear_summary_popup.view.*

class PopUpInventory: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val item: Item = arguments?.get("item") as Item
        val builder = AlertDialog.Builder(context)
        val layout = layoutInflater.inflate(
            R.layout.gear_summary_popup,
            null
        )
        layout.edit_item_button.setOnClickListener {

        }
        layout.delete_item_button.setOnClickListener {

        }
        layout.cancel_button.setOnClickListener {
            findNavController().navigate(R.id.navigation_management_inventory)
            dialog?.dismiss()
        }
        layout.title_text_view.text = item.name
        layout.description_text_view.text = item.description
        builder.setView(layout)

        return builder.create()
    }
}