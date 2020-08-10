package edu.rosehulman.gearlocker.inventory

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.SplashFragment
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.synthetic.main.gear_summary_popup.view.*

class PopUpInventory: DialogFragment() {
    private var listener: SplashFragment.OnLoginButtonPressedListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val item: Item = arguments?.get("item") as Item
        val safeArgs: PopUpInventoryArgs by navArgs()
        val builder = AlertDialog.Builder(context)
        val layout = layoutInflater.inflate(
            R.layout.gear_summary_popup,
            null
        )
        if (!safeArgs.isManagement){
            layout.delete_item_button.isVisible = false
            layout.edit_item_button.text = getString(R.string.add_to_cart)
            layout.edit_item_button.setOnClickListener {
                listener?.onCartItemAdded(item)
                dialog?.dismiss()
                val builder2 = AlertDialog.Builder(context)
                builder2.setTitle("${item.name} added to cart.")
                builder2.setPositiveButton("View Cart"){_,_ ->
                    findNavController().navigate(R.id.cartFragment)
                    dialog?.dismiss()
                }
                builder2.setNegativeButton("Return to Inventory"){_, _ ->
                    findNavController().navigate(R.id.navigation_inventory)
                    dialog?.dismiss()
                }
                builder2.create().show()
            }
            layout.cancel_button.setOnClickListener {
                findNavController().navigate(R.id.navigation_inventory)
                dialog?.dismiss()
            }
        } else {
            layout.edit_item_button.text = getString(R.string.edit)
            layout.delete_item_button.setOnClickListener {
                findNavController().navigate(R.id.deleteFragment)
                dialog?.dismiss()
            }
            layout.edit_item_button.setOnClickListener {
                findNavController().navigate(R.id.addItem)
                dialog?.dismiss()
            }
            layout.cancel_button.setOnClickListener {
                findNavController().navigate(R.id.navigation_management_inventory)
                dialog?.dismiss()
            }
        }
        layout.title_text_view.text = item.name
        layout.description_text_view.text = item.description
        builder.setView(layout)

        return builder.create()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SplashFragment.OnLoginButtonPressedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnLoginButtonPressedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}