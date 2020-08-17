package edu.rosehulman.gearlocker.clubs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        return constraintLayout
    }
}