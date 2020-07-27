package edu.rosehulman.gearlocker.rentals

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.dashboard.DashboardFragment

class ManagementHomeFragment() : Fragment() {
    private var mainActivity: FragmentSwitcher? = null

    fun newInstance(mainActivity: FragmentSwitcher?): ManagementHomeFragment? {
        val m = ManagementHomeFragment()
        m.mainActivity = mainActivity
        return m
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : ConstraintLayout =
            inflater.inflate(R.layout.gear_management_home, container, false) as ConstraintLayout
        view.getViewById(R.id.inventory_button).setOnClickListener {
            Log.d(Constants.TAG, "Inventory clicked from management page")
        }
        view.getViewById(R.id.rentals_button).setOnClickListener{

        }
        view.getViewById(R.id.messages_button).setOnClickListener {

        }
        view.getViewById(R.id.return_to_dash_button).setOnClickListener {
            Log.d(Constants.TAG, "Should switch to the dashboard here from management")
            mainActivity?.switchToFragment(DashboardFragment())
        }
        return view
    }

    interface FragmentSwitcher{
        fun switchToFragment(fragment: Fragment)
    }
}