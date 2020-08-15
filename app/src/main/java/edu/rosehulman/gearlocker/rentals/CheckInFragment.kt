package edu.rosehulman.gearlocker.rentals

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.ImageProducer
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.synthetic.main.check_in.view.*
import kotlinx.android.synthetic.main.management_activity_main.*

class CheckInFragment: Fragment() {

    private val RC_TAKE_PICTURE = 1
    private val RC_CHOOSE_PICTURE = 2

    private val imageProducer = ImageProducer(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args : CheckInFragmentArgs by navArgs()
        var item = args.item
        var rental = args.rental
        val view : RelativeLayout =
            inflater.inflate(R.layout.check_in, container, false) as RelativeLayout
        view.cancel_check_in_button.setOnClickListener {
            findNavController().navigate(R.id.navigation_management_rentals)
        }
        view.check_out_photo.setImageURI(item?.curPhotoPath?.toUri())
        view.gear_name_text_view.text = item?.name
        view.upload_photo.setOnClickListener {
            imageProducer.launchChooseIntent()
            addAndShowImage(view, imageProducer, item)
        }
        view.take_photo.setOnClickListener {
            imageProducer.launchCameraIntent()
            addAndShowImage(view, imageProducer, item)
        }
        view.complete_check_in_button.setOnClickListener {
            var builder = AlertDialog.Builder(this.context)
            builder.setTitle("Check-In Complete")
            Log.d(Constants.TAG, "${item?.name} checked in")
            builder.setPositiveButton("Return to Rentals"){ _, _ ->
                Log.d(Constants.TAG, "Checkin complete")
                args.rentalHandler?.removeItemFromRental(rental!!, item!!)
                findNavController().navigate(R.id.navigation_management_rentals)
            }
            builder.setNeutralButton("Return to Managment Home"){ _, _ ->
                Log.d(Constants.TAG, "Checkin complete")
                findNavController().navigate(R.id.navigation_management_home)
            }
            builder.create().show()
        }
        activity?.nav_host_fragment_management?.findNavController()?.graph?.startDestination = R.id.checkInFragment
        return view
    }

    private fun addAndShowImage(
        view: View,
        imageProducer: ImageProducer,
        item: Item?
    ) {
        imageProducer.add(imageProducer.currentPhotoPath)
        item!!.curPhotoPath = imageProducer.downloadUri.toString()
        Picasso.get().load(imageProducer.downloadUri).into(view.check_in_photo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_TAKE_PICTURE -> {
                    imageProducer.sendCameraPhotoToAdapter()
                }
                RC_CHOOSE_PICTURE -> {
                    imageProducer.sendGalleryPhotoToAdapter(data)
                }
            }
        }
    }
}