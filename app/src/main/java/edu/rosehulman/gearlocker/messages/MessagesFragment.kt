package edu.rosehulman.gearlocker.messages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.AuthProvider
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Message
import kotlinx.android.synthetic.main.fragment_messages.view.*
import kotlinx.android.synthetic.main.management_activity_main.*

class MessagesFragment : Fragment() {
    private lateinit var adapter: MessagesAdapter

    private val messagesRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_MESSAGES)

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

        val uid = (context as AuthProvider).getUID()

        view.send_button.setOnClickListener {
            val receiver = if (uid == "gotharbg") { "sorensej" } else { "gotharbg" }
            messagesRef.add(Message(uid, view.input.text.toString(), receiver))
            view.input.text.clear()
        }

        return view
    }
}