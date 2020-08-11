package edu.rosehulman.gearlocker.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Cart
import edu.rosehulman.gearlocker.models.Item

class CartFragment : Fragment() {

    private var adapter: CartAdapter? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val cart: Cart = arguments?.get("cart") as Cart
        val constraintView : ConstraintLayout =
            inflater.inflate(R.layout.gear_cart, container, false) as ConstraintLayout
        adapter = CartAdapter(requireContext(), cart.arrayList)
        val recyclerView = constraintView.getViewById(R.id.rental_list) as RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        return constraintView
    }
}