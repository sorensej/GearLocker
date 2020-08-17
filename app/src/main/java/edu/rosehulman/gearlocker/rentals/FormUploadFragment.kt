package edu.rosehulman.gearlocker.rentals

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import edu.rosehulman.gearlocker.CameraAndUploadUtils
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Form
import kotlinx.android.synthetic.main.fragment_upload_form.view.*

class FormUploadFragment : Fragment(), CameraAndUploadUtils.OnAddedToStorageListener {

    private val storageRef = FirebaseStorage.getInstance().reference.child("images")
    private var imageUri: String? = null

    private val formsRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_FORMS)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val safeArgs: FormUploadFragmentArgs by navArgs()
        val rental = safeArgs.rental
        val rentalHandler = safeArgs.rentalHandler

        val view = inflater.inflate(R.layout.fragment_upload_form, container, false)
        view.start_date.text = rental.startDate.toString()
        view.end_date.text = rental.endDate.toString()
        view.renter_name.text = rental.uid
        val arrayList = arrayListOf<String>("", "", "")
        view.upload_photo1.setOnClickListener {
            CameraAndUploadUtils.startPickActivity(this)
            arrayList[0] = imageUri.toString()
            Picasso.get().load(imageUri).into(view?.photo1)
        }
        view.upload_photo2.setOnClickListener {
            CameraAndUploadUtils.startPickActivity(this)
            arrayList[1] = imageUri.toString()
            Picasso.get().load(imageUri).into(view?.photo2)
        }
        view.upload_photo3.setOnClickListener {
            CameraAndUploadUtils.startPickActivity(this)
            arrayList[2] = imageUri.toString()
            Picasso.get().load(imageUri).into(view?.photo3)
        }
        view.take_photo1.setOnClickListener {
            CameraAndUploadUtils.startCameraActivity(this)
            arrayList[0] = imageUri.toString()
            Picasso.get().load(imageUri).into(view?.photo1)
        }
        view.take_photo2.setOnClickListener {
            CameraAndUploadUtils.startCameraActivity(this)
            arrayList[1] = imageUri.toString()
            Picasso.get().load(imageUri).into(view?.photo2)
        }
        view.take_photo3.setOnClickListener {
            CameraAndUploadUtils.startCameraActivity(this)
            arrayList[2] = imageUri.toString()
            Picasso.get().load(imageUri).into(view?.photo3)
        }
        view.submit_button.setOnClickListener {
            try {
                var form = Form(
                    rental.startDate,
                    rental.endDate,
                    rental.uid,
                    arrayList,
                    true
                )
                formsRef.add(form)
                rentalHandler.onGetNavController().navigate(R.id.rentalsOverviewManagment)
                rentalHandler.confirmRental(rental, 0, 0)
            } catch (e: Exception) {
                Log.e(Constants.TAG, "$e")
            }
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CameraAndUploadUtils.RC_TAKE_PICTURE   -> {
                    CameraAndUploadUtils.addToStorage(
                        storageRef,
                        data?.extras?.get("data") as Bitmap,
                        this.imageUri,
                        this
                    )
                }
                CameraAndUploadUtils.RC_CHOOSE_PICTURE -> {
                    val stream = context?.contentResolver?.openInputStream(data?.data!!)
                    CameraAndUploadUtils.addToStorage(
                        storageRef,
                        BitmapFactory.decodeStream(stream),
                        this.imageUri,
                        this
                    )
                }
            }
        }
    }

    override fun onAddedToStorage(uriString: String) {
        this.imageUri = uriString
    }
}