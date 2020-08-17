package edu.rosehulman.gearlocker.managment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.AuthProvider
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Club
import kotlinx.android.synthetic.main.gear_management_sign_in.view.*

class ManagementSignInFragment: Fragment() {

    private val clubsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_CLUBS)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : RelativeLayout =
            inflater.inflate(R.layout.gear_management_sign_in, container, false) as RelativeLayout
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView?.isVisible = false

        val uid = (context as AuthProvider).getUID()
        Log.d(Constants.TAG, uid)

        view.manage_gear_button.setOnClickListener {
            var signedIn = false
            clubsRef.get().addOnSuccessListener { querySnapshot ->
                for (doc in querySnapshot.documents) {
                    val club = Club.fromSnapshot(doc)
                    if (club.isAdmin(uid, view.passcode_edit_text.text.toString())) {
                        signedIn = true
                        val bundle = bundleOf("clubID" to club.id)
                        findNavController().navigate(R.id.managementMainActivity, bundle)
                        return@addOnSuccessListener
                    }
                }
            }
            if (!signedIn) {
                view.login_error_textview.visibility = View.VISIBLE
            }
        }
        view.return_to_dash_button_login.setOnClickListener {
            bottomNavigationView?.isVisible = true
            findNavController().navigate(R.id.navigation_dashboard)
        }
        return view
    }
}