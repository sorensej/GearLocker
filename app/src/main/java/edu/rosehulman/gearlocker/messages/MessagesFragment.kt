package edu.rosehulman.gearlocker.messages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import kotlinx.android.synthetic.main.fragment_messages.view.*
import kotlinx.android.synthetic.main.management_activity_main.*

class MessagesFragment : Fragment() {
    private lateinit var adapter: MessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_messages, container, false)
        val manager = LinearLayoutManager(context)
        val safeArgs: MessagesFragmentArgs by navArgs()
        if(safeArgs.isManagement){
            activity?.nav_host_fragment_management?.findNavController()?.graph?.startDestination = R.id.navigation_management_messages
        }else{
            activity?.findNavController(R.id.nav_host_fragment)?.graph?.startDestination = R.id.navigation_messages
        }
        Log.d(Constants.TAG, "Fragment: Messages")
        adapter = MessagesAdapter(requireContext())
        view.list_of_messages.adapter = adapter
        view.list_of_messages.layoutManager = manager
        Log.d(Constants.TAG, "${safeArgs.isManagement}")
        return view
    }
}