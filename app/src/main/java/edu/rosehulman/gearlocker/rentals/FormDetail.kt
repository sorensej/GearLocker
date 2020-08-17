package edu.rosehulman.gearlocker.rentals

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import kotlinx.android.synthetic.main.form_detail.view.*

class FormDetail : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val safeArgs: FormDetailArgs by navArgs()
        val form = safeArgs.form
        val view = inflater.inflate(R.layout.form_detail, container, false)
        view.dates.text = "${form.startDate} to ${form.endDate}"
        view.name.text = form.uid
        Log.d(Constants.TAG, "${form.images}")
        Picasso.get().load(form.images[0]).into(view.page1)
        Picasso.get().load(form.images[1]).into(view.page2)
        Picasso.get().load(form.images[2]).into(view.page3)

        return view
    }
}