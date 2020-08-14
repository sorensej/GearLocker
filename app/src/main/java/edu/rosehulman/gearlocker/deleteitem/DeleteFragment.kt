package edu.rosehulman.gearlocker.deleteitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.R

class DeleteFragment : Fragment() {

    private var adapter: DeleteAdapter? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val safeArgs: DeleteFragmentArgs by navArgs()
        val recyclerView : RecyclerView =
            inflater.inflate(R.layout.management_delete_items, container, false) as RecyclerView
        adapter = DeleteAdapter(requireContext(), safeArgs.itemCatagory!!.items, safeArgs.inventoryFragment)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        return recyclerView
    }
}