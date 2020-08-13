package edu.rosehulman.gearlocker.inventory

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
        val item: Item = arguments?.get("item") as Item
        val safeArgs: PopUpInventoryArgs by navArgs()
        val layout = inflater.inflate(R.layout.gear_summary_popup, container, true)
        if (item.curPhotoPath!= ""){
            layout.item_photo.setImageURI(item.curPhotoPath.toUri())
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
                    val bundle = Bundle()
                    bundle.putParcelable("cart", listener?.onGetCart())
                    listener?.onGetNavController()?.navigate(R.id.cartFragment, bundle)
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
        layout.cost_text_view.text = String.format("Est. Value: $%.2f", item.estimatedCost)

        return layout
    }
}