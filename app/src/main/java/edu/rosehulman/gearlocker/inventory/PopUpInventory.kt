package edu.rosehulman.gearlocker.inventory

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.SplashFragment
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.synthetic.main.gear_summary_popup.view.*

class PopUpInventory: DialogFragment() {
    private var listener: SplashFragment.ApplicationNavigationListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val safeArgs: PopUpInventoryArgs by navArgs()
        val item: Item = safeArgs.item
        val layout = inflater.inflate(R.layout.gear_summary_popup, container, true)
        if (item.curPhotoPath!= ""){
            Picasso.get()
                .load(item.curPhotoPath)
                .into(layout.item_photo)
            
        }
        if (!safeArgs.isManagement){
            listener = activity as SplashFragment.ApplicationNavigationListener
            layout.edit_item_button.text = getString(R.string.add_to_cart)
            layout.edit_item_button.setOnClickListener {
                listener?.onCartItemAdded(item)
                dialog?.dismiss()
                val builder2 = AlertDialog.Builder(context)
                builder2.setTitle("${item.name} added to cart.")
                builder2.setPositiveButton("View Cart"){_,_ ->
                    safeArgs.itemInterface?.onRentItem(item)
                    val bundle = Bundle()
                    bundle.putParcelable("cart", listener?.onGetCart())
                    bundle.putParcelable("itemInterface", safeArgs.itemInterface)
                    listener?.onGetNavController()?.navigate(R.id.cartFragment, bundle)
                    dialog?.dismiss()
                }
                builder2.setNegativeButton("Return to Inventory"){_, _ ->
                    safeArgs.itemInterface?.onRentItem(item)
                    safeArgs.itemInterface?.onNavControllerRequest()?.navigate(R.id.navigation_inventory)
                    dialog?.dismiss()
                }
                builder2.create().show()
            }
            layout.cancel_button.setOnClickListener {
                safeArgs.itemInterface?.onNavControllerRequest()?.navigate(R.id.navigation_inventory)
                dialog?.dismiss()
            }
        } else {
            layout.edit_item_button.text = getString(R.string.edit)
            layout.edit_item_button.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("item", item)
                bundle.putParcelable("itemInterface", safeArgs.itemInterface)
                bundle.putBoolean("isAdd", false)
                findNavController().navigate(R.id.addItem, bundle)
                dialog?.dismiss()
            }
            layout.cancel_button.setOnClickListener {
                findNavController().navigate(R.id.navigation_management_inventory)
                dialog?.dismiss()
            }
        }
        layout.title_text_view.text = item.name
        layout.description_text_view.text = item.description
        layout.cost_text_view.text = String.format("Est. Value: $%.2f", item.estimatedCost)
       layout.current_condition_text_view.text = when (item.condition) {
            1    -> "Replace"
            2    -> "Bad"
            3    -> "Average"
            4    -> "Good"
            5    -> "New"
            else -> "No Condition"
        }
        return layout
    }
}