package edu.rosehulman.gearlocker.rentals

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import edu.rosehulman.gearlocker.ManagementMainActivity
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.dashboard.DashboardFragment
import kotlinx.android.synthetic.main.gear_management_sign_in.view.*

class ManagementSignInFragment: Fragment() {

    var mainActivity: ManagementHomeFragment.FragmentSwitcher? = null

    fun newInstance(mainActivity: ManagementHomeFragment.FragmentSwitcher?): ManagementSignInFragment {
        val m = ManagementSignInFragment()
        m.mainActivity = mainActivity
        return m
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : RelativeLayout =
            inflater.inflate(R.layout.gear_management_sign_in, container, false) as RelativeLayout
        view.manage_gear_button.setOnClickListener {
            startActivity(Intent(this.context, ManagementMainActivity::class.java))
        }
        view.return_to_dash_button_login.setOnClickListener {
            mainActivity?.switchToFragment(DashboardFragment())
        }
        return view
    }
}