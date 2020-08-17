package edu.rosehulman.gearlocker.cart

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.rosehulman.gearlocker.AuthProvider
import edu.rosehulman.gearlocker.ClubProvider
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.inventory.InventoryAdapter
import edu.rosehulman.gearlocker.models.Cart
import kotlinx.android.synthetic.main.gear_cart.view.*
import java.util.*

class CartFragment : Fragment() {

    private val rentalsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_RENTALS)

    private var adapter: CartAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val safeArgs: CartFragmentArgs by navArgs()
        val cart: Cart = safeArgs.cart
        val itemInterface: InventoryAdapter.ItemInterface = safeArgs.itemInterface
        val constraintView: ConstraintLayout =
            inflater.inflate(R.layout.gear_cart, container, false) as ConstraintLayout
        adapter = CartAdapter(requireContext(), cart.arrayList)
        val recyclerView = constraintView.getViewById(R.id.rental_list) as RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)


        val totalCost = if (cart.arrayList.isNotEmpty()) {
            cart.arrayList.map { it.estimatedCost }.reduce { acc, fl -> acc + fl }
        } else {
            0.0f
        }

        constraintView.rental_total_number.text = String.format("$%.2f", totalCost)

        val startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()

        if (cart.currentStartDate != null) {
            startCalendar.time = cart.currentStartDate!!
        }
        if (cart.currentEndDate != null) {
            endCalendar.time = cart.currentEndDate!!
        }


        constraintView.start_date_button.setOnClickListener {
            DatePickerDialog(
                requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    startCalendar.set(year, month, dayOfMonth, 0, 0, 0)
                    constraintView.start_date_button.text = "$month/$dayOfMonth/$year"
                    cart.currentStartDate = startCalendar.time
                },
                startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        constraintView.end_date_button.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    endCalendar.set(year, month, dayOfMonth, 0, 0, 0)
                    constraintView.end_date_button.text = "$month/$dayOfMonth/$year"
                    cart.currentEndDate = endCalendar.time
                },
                endCalendar.get(Calendar.YEAR),
                endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        constraintView.fab.setOnClickListener {
            for (item in cart.arrayList) {
                itemInterface.onRentItem(item)
            }
            try {

                val rental = cart.toRental(
                    (requireContext() as AuthProvider).getUID(),
                    cart.currentStartDate!!,
                    cart.currentEndDate!!,
                    (requireContext() as ClubProvider).getActiveClubID()
                )
                rentalsRef.add(rental)
                var builder = AlertDialog.Builder(context)
                builder.setTitle("${rental.startDate} to ${rental.endDate}")
                builder.setMessage("Rental request received. You should receive a message to pick up your rental in approximately 3 days.")
                builder.setPositiveButton("Return to Dashboard") { dialogInterface: DialogInterface, i: Int ->
                    findNavController().navigate(R.id.navigation_dashboard)
                }
                builder.create().show()
            } catch (e: Exception){
                var builder = AlertDialog.Builder(context)
                builder.setTitle("Incomplete Input")
                builder.setMessage("Please make sure you entered start and end dates.")
                builder.setPositiveButton("Okay") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                builder.create().show()
            }
        }
        Log.d(Constants.TAG, (requireContext() as AuthProvider).getUID())
        return constraintView
    }
}