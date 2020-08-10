package edu.rosehulman.gearlocker.dashboard

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.DemoData
import edu.rosehulman.gearlocker.R

class DashboardFragment : Fragment() {

    private var adapter: DashboardAdapter? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val recyclerView : RecyclerView =
            inflater.inflate(R.layout.fragment_dashboard, container, false) as RecyclerView
        adapter = DashboardAdapter(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        setHasOptionsMenu(true)

        return recyclerView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_demo_data -> {
                Log.d(Constants.TAG, "test2")
                DemoData.createRentals()
                true
            }
            R.id.add_club->{
                findNavController().navigate(R.id.clubsFragment)
                true
            }
            R.id.log_out->{

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}