package edu.rosehulman.gearlocker.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.gearlocker.R
import kotlinx.android.synthetic.main.fragment_rental_overview.view.*

class DashboardFragment : Fragment() {

    private var curAdapter: DashboardAdapter? = null
    private var pastAdapter: DashboardDeletedAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val constraintView: ConstraintLayout = inflater.inflate(R.layout.new_dash, container, false) as ConstraintLayout
        curAdapter = DashboardAdapter(requireContext())
        pastAdapter =
            DashboardDeletedAdapter(requireContext())
        constraintView.cur_rentals_recyclerview.adapter = curAdapter
        constraintView.rental_requests_recyclerview.adapter = pastAdapter
        constraintView.cur_rentals_recyclerview.layoutManager = LinearLayoutManager(context)
        constraintView.rental_requests_recyclerview.layoutManager = LinearLayoutManager(context)
        return constraintView
    }
}