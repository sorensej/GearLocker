package edu.rosehulman.gearlocker.inventory

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.DemoData
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.synthetic.main.fragment_inventory.view.*
import kotlinx.android.synthetic.main.management_activity_main.*


class InventoryFragment : Fragment(), InventoryAdapter.ItemInterface {

    private lateinit var _adapter: InventoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inventory, container, false)
        val safeArgs: InventoryFragmentArgs by navArgs()
        if(safeArgs.isManagement){
            activity?.nav_host_fragment_management?.findNavController()?.graph?.startDestination = R.id.navigation_management_inventory
            Log.d(Constants.TAG, "Management detected.")
            view.fab.setImageResource(R.drawable.ic_add)
            view.fab.setOnClickListener {
                findNavController().navigate(R.id.addItem)
            }
        }else{
            activity?.findNavController(R.id.nav_host_fragment)?.graph?.startDestination = R.id.navigation_inventory
            view.fab.setImageResource(R.drawable.ic_cart)
            Log.d(Constants.TAG, "Not management activity.")
        }
        val manager = LinearLayoutManager(context)
        _adapter = InventoryAdapter(
            requireContext(), this
        )
        view?.inventory_recycler_view?.adapter = _adapter
        view?.inventory_recycler_view?.layoutManager = manager

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_demo_data -> {
                DemoData.createItemCategories()
                true
            }
            R.id.add_club->{
                findNavController().navigate(R.id.clubsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemSelected(position: Int) {
        val item = Item("Name", 20f, 3, "description")
        var bundle = Bundle()
        bundle.putParcelable("item", item)
        findNavController().navigate(R.id.popUpInventory, bundle)

    }
}