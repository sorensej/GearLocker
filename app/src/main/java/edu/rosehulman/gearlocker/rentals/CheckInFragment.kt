package edu.rosehulman.gearlocker.rentals

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import edu.rosehulman.gearlocker.CameraAndUploadUtils
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import kotlinx.android.synthetic.main.check_in.view.*
import kotlinx.android.synthetic.main.management_activity_main.*

class CheckInFragment: Fragment(), CameraAndUploadUtils.OnAddedToStorageListener {

    private val storageRef = FirebaseStorage.getInstance().reference.child("images")
    private var imageUri: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args : CheckInFragmentArgs by navArgs()
        val item = args.item
        val rental = args.rental
        val view : RelativeLayout =
            inflater.inflate(R.layout.check_in, container, false) as RelativeLayout
        Picasso.get().load(item?.curPhotoPath).into(view.check_out_photo)
        view.gear_name_text_view.text = item?.name
        view.condition.text = item?.condition.toString()
        view.dates.text = "${rental?.startDate} to ${rental?.endDate}"
        view.upload_photo.setOnClickListener {
            CameraAndUploadUtils.startPickActivity(this)
        }
        view.take_photo.setOnClickListener {
            CameraAndUploadUtils.startCameraActivity(this)
        }
        view.skip_photo.setOnClickListener {
            imageUri = item!!.curPhotoPath
            Picasso.get().load(item.curPhotoPath).into(view.check_in_photo)
        }
        view.complete_check_in_button.setOnClickListener {
            val ref = FirebaseStorage.getInstance().getReferenceFromUrl(item!!.curPhotoPath)
            ref.delete()
            item.curPhotoPath = imageUri!!

            FirebaseFirestore
                .getInstance()
                .collection(Constants.FB_ITEMS)
                .document(item.id)
                .update("curPhotoPath", imageUri)

            val builder = AlertDialog.Builder(this.context)
            builder.setTitle("Check-In Complete")
            Log.d(Constants.TAG, "${item?.name} checked in")
            builder.setPositiveButton("Return to Rentals"){ _, _ ->
                Log.d(Constants.TAG, "Checkin complete")
                args.rentalHandler?.removeItemFromRental(rental!!, item)
                findNavController().navigate(R.id.navigation_management_rentals)
            }
            builder.setNeutralButton("Return to Managment Home"){ _, _ ->
                Log.d(Constants.TAG, "Checkin complete")
                findNavController().navigate(R.id.navigation_management_home)
            }
            builder.create().show()
        }
        view.cancel_check_in_button.setOnClickListener {
            if (this.imageUri != null) {
                val ref = FirebaseStorage.getInstance().getReferenceFromUrl(this.imageUri!!)
                ref.delete()
            }
            findNavController().navigate(R.id.navigation_management_rentals)
        }
        activity?.nav_host_fragment_management?.findNavController()?.graph?.startDestination = R.id.checkInFragment
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CameraAndUploadUtils.RC_TAKE_PICTURE -> {
                    CameraAndUploadUtils.addToStorage(
                        storageRef,
                        data?.extras?.get("data") as Bitmap,
                        this.imageUri,
                        this)
                }
                CameraAndUploadUtils.RC_CHOOSE_PICTURE -> {
                    val stream = context?.contentResolver?.openInputStream(data?.data!!)
                    CameraAndUploadUtils.addToStorage(
                        storageRef,
                        BitmapFactory.decodeStream(stream),
                        this.imageUri,
                        this)
                }
            }
        }
    }

    override fun onAddedToStorage(uriString: String) {
       Picasso.get().load(uriString).into(view?.check_in_photo)
       this.imageUri = uriString
    }
}