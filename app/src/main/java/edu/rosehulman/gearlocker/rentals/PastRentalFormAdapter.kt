package edu.rosehulman.gearlocker.rentals

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Form
import kotlinx.android.synthetic.main.inventory_sub_item.view.*

class PastRentalFormAdapter(
    val context: Context,
    val findNavController: NavController
) : RecyclerView.Adapter<PastRentalFormAdapter.PastRentalFormViewHolder>() {

    private val pastForms = arrayListOf<Form>()

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

            if (!form.current) {

                when (change.type) {
                    DocumentChange.Type.ADDED    -> {
                        pastForms.add(form)
                        notifyItemInserted(0)
                    }
                    DocumentChange.Type.REMOVED  -> {
                        val position = pastForms.indexOfFirst { it.id == form.id }
                        if (position!=-1) {
                            pastForms.removeAt(position)
                            notifyItemRemoved(position)
                        }
                    }
                    DocumentChange.Type.MODIFIED -> {
                        val position = pastForms.indexOfFirst { it.id == form.id }
                        if (position!=-1) {
                            pastForms[position] = form
                            notifyItemChanged(position)
                        }
                    }
                }
            }
        }
    }

    inner class PastRentalFormViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            form: Form
        ) {
            itemView.sub_item_name.text = "${form.startDate} to ${form.endDate} by ${form.uid}"
            itemView.setOnClickListener {
                var bundle= Bundle()
                bundle.putParcelable("form", form)
                findNavController.navigate(R.id.formDetail, bundle)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastRentalFormAdapter.PastRentalFormViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.inventory_sub_item_simple, parent, false)
        return PastRentalFormViewHolder(view)
    }

    override fun getItemCount() = pastForms.size

    override fun onBindViewHolder(holder: PastRentalFormViewHolder, position: Int) {
        holder.bind(pastForms[position])
    }


}