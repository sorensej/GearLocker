package edu.rosehulman.gearlocker.rentals

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Form
import kotlinx.android.synthetic.main.inventory_sub_item.view.*

class CurrentRentalFormAdapter(val context: Context) : RecyclerView.Adapter<CurrentRentalFormAdapter.CurrentRentalFormViewHolder>() {

    private val currentForms = arrayListOf<Form>()

    private val formsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_FORMS)

    init {
        formsRef.addSnapshotListener { snapshot, exception ->
            handleSnapshotEvent(snapshot, exception)
        }
        notifyDataSetChanged()
    }

    private fun handleSnapshotEvent(
        snapshot: QuerySnapshot?,
        exception: FirebaseFirestoreException?
    ) {
        if (exception != null) {
            Log.e(Constants.TAG, "Inventory Listen Error: $exception")
            return
        }

        for (change in snapshot!!.documentChanges) {
            val form = Form.fromSnapshot(change.document)

            if (form.current) {

                when (change.type) {
                    DocumentChange.Type.ADDED   -> {
                        currentForms.add(form)
                        notifyItemInserted(0)
                    }
                    DocumentChange.Type.REMOVED -> {
                        val position = currentForms.indexOfFirst { it.id == form.id }
                        currentForms.removeAt(position)
                        notifyItemRemoved(position)
                    }
                    DocumentChange.Type.MODIFIED -> {
                        val position = currentForms.indexOfFirst { it.id == form.id }
                        currentForms[position] = form
                        notifyItemChanged(position)
                    }
                }
            }
        }
    }

    inner class CurrentRentalFormViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                //TODO: Show the form here
            }
        }

        fun bind(form: Form) {
            itemView.sub_item_name.text = "${form.startDate} to ${form.endDate} by ${form.uid}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentRentalFormViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.inventory_sub_item_simple, parent, false) // TODO: Replace the stand-in layout
        return CurrentRentalFormViewHolder(view)
    }

    override fun getItemCount() = currentForms.size

    override fun onBindViewHolder(holder: CurrentRentalFormViewHolder, position: Int) {
        holder.bind(currentForms[position])
    }
}