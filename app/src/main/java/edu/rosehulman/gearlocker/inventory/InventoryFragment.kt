package edu.rosehulman.gearlocker.inventory

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.SplashFragment
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.dialog_add_option.view.*
import kotlinx.android.synthetic.main.fragment_inventory.view.*
import kotlinx.android.synthetic.main.management_activity_main.*

@Parcelize
class InventoryFragment : Fragment(), InventoryAdapter.ItemInterface, Parcelable {

    private lateinit var _adapter: InventoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inventory, container, false)
        val safeArgs: InventoryFragmentArgs by navArgs()
        if (safeArgs.isManagement) {
            activity?.nav_host_fragment_management?.findNavController()?.graph?.startDestination =
                R.id.navigation_management_inventory
            Log.d(Constants.TAG, "Management detected.")
            view.fab.setImageResource(R.drawable.ic_add)
            view.fab.setOnClickListener {
                showAddOption()
            }
        } else {
            activity?.findNavController(R.id.nav_host_fragment)?.graph?.startDestination =
                R.id.navigation_inventory
            view.fab.setImageResource(R.drawable.ic_cart)
            view.fab.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable(
                    "cart",
                    (activity as SplashFragment.ApplicationNavigationListener).onGetCart()
                )
                bundle.putParcelable("itemInterface", this as InventoryAdapter.ItemInterface)
                findNavController().navigate(R.id.cartFragment, bundle)
            }
            Log.d(Constants.TAG, "Not management activity.")
        }
        val manager = LinearLayoutManager(context)
        _adapter = InventoryAdapter(
            requireContext(), this, safeArgs.isManagement
        )
        view?.inventory_recycler_view?.adapter = _adapter
        view?.inventory_recycler_view?.layoutManager = manager
        return view
    }

    override fun onItemSelected(item: Item) {
        val bundle = Bundle()
        bundle.putParcelable("item", item)
        bundle.putParcelable("itemInterface", this as InventoryAdapter.ItemInterface)
        findNavController().navigate(R.id.popUpInventory, bundle)
    }

    private fun showAddOption() {
        val builder = AlertDialog.Builder(context)

        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_option, null)

        builder.setView(view)

        val dialog = builder.create()

        view.cancel_button.setOnClickListener { dialog.dismiss() }
        view.add_item_button.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("itemInterface", this as InventoryAdapter.ItemInterface)
            findNavController().navigate(R.id.addItem, bundle)
            dialog.dismiss()
        }
        view.add_category_button.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.addItemCategory)
        }
        dialog.show()
    }

    override fun onItemAdded(item: Item) {
        _adapter.add(item)
    }

    override fun onNavControllerRequest(): NavController {
        return findNavController()
    }

    override fun onGetAdapter(): InventoryAdapter {
        return _adapter
    }

    override fun onEditItem(item: Item) {
        _adapter.editItem(item)
    }

    override fun onRentItem(item: Item) {
        _adapter.rentItem(item)
    }

}