package edu.rosehulman.gearlocker.rentals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.R
import kotlinx.android.synthetic.main.fragment_form_overview.view.*

class FormOverviewFragment : Fragment() {

    private var currentAdapter: CurrentRentalFormAdapter? = null
    private var pastAdapter: PastRentalFormAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_form_overview, container, false)
        currentAdapter = context?.let { CurrentRentalFormAdapter(it) }
        view.current_rental_recycler_view.adapter = currentAdapter
        view.current_rental_recycler_view.layoutManager = LinearLayoutManager(context)

        pastAdapter = context?.let { PastRentalFormAdapter(it) }
        view.past_rental_recycler_view.adapter = pastAdapter
        view.past_rental_recycler_view.layoutManager = LinearLayoutManager(context)

        return view
    }
}