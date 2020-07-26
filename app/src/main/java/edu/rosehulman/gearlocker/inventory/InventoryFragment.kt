package edu.rosehulman.gearlocker.inventory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.gearlocker.R
import kotlinx.android.synthetic.main.fragment_inventory.view.*

class InventoryFragment : Fragment() {

    private lateinit var _adapter: InventoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("GearLocker", "test")
        val view = inflater.inflate(R.layout.fragment_inventory, container, false)

        val manager = LinearLayoutManager(context)

        //view.inventory_recycler_view.setHasFixedSize(true)
        _adapter = InventoryAdapter(
            requireContext()
        )
        view.inventory_recycler_view.adapter = _adapter
        view.inventory_recycler_view.layoutManager = manager

        return view
    }
}