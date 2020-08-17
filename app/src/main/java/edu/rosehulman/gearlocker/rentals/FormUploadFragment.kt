package edu.rosehulman.gearlocker.rentals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import edu.rosehulman.gearlocker.R
import kotlinx.android.synthetic.main.fragment_upload_form.view.*

class FormUploadFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val safeArgs: FormUploadFragmentArgs by navArgs()
        val rental = safeArgs.rental

        val view = inflater.inflate(R.layout.fragment_upload_form, container, false)
        view.start_date.text = rental.startDate.toString()
        view.end_date.text = rental.endDate.toString()
        view.renter_name.text = rental.uid
        view.submit_button.setOnClickListener {

        }
        return view
    }
}