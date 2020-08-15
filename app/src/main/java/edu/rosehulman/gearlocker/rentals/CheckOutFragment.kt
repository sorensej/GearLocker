package edu.rosehulman.gearlocker.rentals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import edu.rosehulman.gearlocker.R
import kotlinx.android.synthetic.main.management_activity_main.*

class CheckOutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : ConstraintLayout =
            inflater.inflate(R.layout.check_out_view, container, false) as ConstraintLayout
        activity?.nav_host_fragment_management?.findNavController()?.graph?.startDestination = R.id.checkInFragment
        return view
    }
}