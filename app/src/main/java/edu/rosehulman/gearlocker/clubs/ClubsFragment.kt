package edu.rosehulman.gearlocker.clubs

import android.app.SearchManager
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Club
import kotlinx.android.synthetic.main.search.view.*

class ClubsFragment : Fragment() {

    var clubs: ArrayList<Club> = ArrayList<Club>()

    private val currentClubsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_CLUBS)

    init {
        currentClubsRef.addSnapshotListener { snapshot, exception ->
            handleSnapshotEvent(snapshot, exception)
        }

    }

    private fun handleSnapshotEvent(
        snapshot: QuerySnapshot?,
        exception: FirebaseFirestoreException?
    ) {
        if (exception != null) {
            Log.e(Constants.TAG, "Club Listen Error: $exception")
            return
        }

        for (change in snapshot!!.documentChanges) {
            val club = Club.fromSnapshot(change.document)

            when (change.type) {
                DocumentChange.Type.ADDED    -> {
                    clubs.add(0, club)
                }
                DocumentChange.Type.REMOVED  -> {
                    val position = clubs.indexOfFirst { it.id == club.id }
                    clubs.removeAt(position)
                }
                DocumentChange.Type.MODIFIED -> {
                    val position = clubs.indexOfFirst { it.id == club.id }
                    clubs[position] = club
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val constraintLayout = inflater.inflate(R.layout.search, container, false)
        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.club_textview)

        val cursorAdapter = SimpleCursorAdapter(
            context,
            R.layout.club_cardview,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )
        //cursorAdapter.setViewResource(R.id.add_club)
//        cursorAdapter.setViewBinder { view, cursor, columnIndex ->
//            view.add_club_textbutton.setOnClickListener {
//                val button: TextView = view.add_club_textbutton
//                val club: Club = clubs[cursor.position]
//
//
//                val auth = FirebaseAuth.getInstance().currentUser
//                if (club.members.containsValue(auth?.uid)) {
//                    button.text = requireContext().getString(R.string.leave_club)
//                    button.setBackgroundResource(R.drawable.round_button_red)
//                    button.setOnClickListener {
//                        removeFromClub(club, auth)
//                    }
//                } else {
//                    button.text = requireContext().getString(R.string.add_club)
//                    button.setBackgroundResource(R.drawable.round_button)
//                    button.setOnClickListener {
//                        addToClub(club, auth)
//                    }
//                }
//            }
//            return@setViewBinder true
//        }
        cursorAdapter.notifyDataSetChanged()
        constraintLayout.searchView.suggestionsAdapter = cursorAdapter
        constraintLayout.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val cursor =
                    MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                query?.let {
                    clubs.forEachIndexed { index, suggestion ->
                        if (suggestion.name.contains(query, true))
                            cursor.addRow(arrayOf(index, suggestion.name))
                    }
                }
                cursorAdapter.changeCursor(cursor)
                return true
            }
        })

        constraintLayout.searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
//                val cursor = constraintLayout.searchView.suggestionsAdapter.getItem(position) as Cursor
//                val selection =
//                    cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
//                constraintLayout.searchView.setQuery(selection, false)
//
//                // Do something with selection
                return true
            }

        })

//        val constraintLayout : ConstraintLayout =
//            inflater.inflate(R.layout.add_club_home, container, false) as ConstraintLayout
//        adapter = ClubsAdapter(requireContext())
//        constraintLayout.findViewById<RecyclerView>(R.id.listView).adapter = adapter
//        constraintLayout.findViewById<RecyclerView>(R.id.listView).layoutManager = LinearLayoutManager(context)
        return constraintLayout
    }

    private fun addToClub(
        club: Club,
        auth: FirebaseUser?
    ) {
        if (auth != null) {
            val displayName = if (!auth.displayName.isNullOrEmpty()) {
                auth.displayName!!
            } else {
                auth.uid
            }
            club.members.putIfAbsent(displayName, auth.uid)
            Log.d(Constants.TAG, club.members.toString())
            FirebaseFirestore
                .getInstance()
                .collection(Constants.FB_CLUBS)
                .document(club.id)
                .set(club)


        }
    }

    private fun removeFromClub(
        club: Club,
        auth: FirebaseUser?
    ) {
        if (auth != null) {
            val displayName = if (!auth.displayName.isNullOrEmpty()) {
                auth.displayName!!
            } else {
                auth.uid
            }

            if (!club.members.containsKey(displayName)) {
                return
            }

            club.members.remove(displayName)
            Log.d(Constants.TAG, club.members.toString())
            FirebaseFirestore
                .getInstance()
                .collection(Constants.FB_CLUBS)
                .document(club.id)
                .set(club)
        }
    }
}