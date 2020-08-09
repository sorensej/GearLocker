package edu.rosehulman.gearlocker.clubs

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.DemoData
import edu.rosehulman.gearlocker.R

class ClubsFragment : Fragment() {

    private var adapter: ClubsAdapter? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val constraintLayout : ConstraintLayout =
            inflater.inflate(R.layout.add_club_home, container, false) as ConstraintLayout
        adapter = ClubsAdapter(requireContext())
        constraintLayout.findViewById<RecyclerView>(R.id.listView).adapter = adapter
        constraintLayout.findViewById<RecyclerView>(R.id.listView).layoutManager = LinearLayoutManager(context)

        setHasOptionsMenu(true)
        return constraintLayout
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_demo_data -> {
                DemoData.createClubs()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}