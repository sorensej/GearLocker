package edu.rosehulman.gearlocker.messages

import android.app.AlertDialog
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
import edu.rosehulman.gearlocker.ClubProvider
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Club
import edu.rosehulman.gearlocker.models.Message
import kotlinx.android.synthetic.main.fragment_messages.view.*
import kotlinx.android.synthetic.main.management_activity_main.*
import java.text.SimpleDateFormat

class MessagesFragment : Fragment() {
    private lateinit var adapter: MessagesAdapter

    private val messagesRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_MESSAGES)

    private val dateFormat = SimpleDateFormat("MM/dd/yyy hh:mm a")

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

        val uid = (context as AuthProvider).getUID()
        val clubID = (context as ClubProvider).getActiveClubID()

        adapter = MessagesAdapter(requireContext(), uid, clubID)
        view.list_of_messages.adapter = adapter
        view.list_of_messages.layoutManager = manager
        Log.d(Constants.TAG, "${safeArgs.isManagement}")

        view.send_button.setOnClickListener {
            var message = Message(uid, view.input.text.toString(), clubID)
            message.sentTimestamp = com.google.firebase.Timestamp.now()
            messagesRef.add(message)
            view.input.text.clear()
        }

        if (safeArgs.isManagement) {
            view.send_button.setOnLongClickListener {
                val builder = AlertDialog.Builder(requireContext())
                FirebaseFirestore
                    .getInstance()
                    .collection(Constants.FB_CLUBS)
                    .document(clubID)
                    .get().addOnSuccessListener { querySnapshot ->
                        val club = Club.fromSnapshot(querySnapshot)
                        val clubMemberIDs = club.members.values.toTypedArray()
                        val clubMemberNames = club.members.keys.toTypedArray()

                        builder.setItems(clubMemberNames) { _, which ->
                            adapter =
                                MessagesAdapter(requireContext(), clubID, clubMemberIDs[which])
                            view.list_of_messages.adapter = adapter
                        }
                        builder.create().show()
                    }
                true
            }
        }
        return view
    }
}