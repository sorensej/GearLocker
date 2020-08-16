package edu.rosehulman.gearlocker.rentals

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import kotlinx.android.synthetic.main.check_in.view.*
import kotlinx.android.synthetic.main.management_activity_main.*
import java.io.ByteArrayOutputStream
import kotlin.math.abs
import kotlin.random.Random

class CheckInFragment: Fragment() {

    private val RC_TAKE_PICTURE = 1
    private val RC_CHOOSE_PICTURE = 2

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
        view.cancel_check_in_button.setOnClickListener {
            findNavController().navigate(R.id.navigation_management_rentals)
        }
        view.check_out_photo.setImageURI(item?.curPhotoPath?.toUri())
        view.gear_name_text_view.text = item?.name
        view.upload_photo.setOnClickListener {
            startPickActivity()
        }
        view.take_photo.setOnClickListener {
            startCameraActivity()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_TAKE_PICTURE -> {
                    addToStorage(data?.extras?.get("data") as Bitmap)
                }
                RC_CHOOSE_PICTURE -> {
                    val stream = context?.contentResolver?.openInputStream(data?.data!!)
                    addToStorage(BitmapFactory.decodeStream(stream))
                }
            }
        }
    }

    private fun startCameraActivity() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, RC_TAKE_PICTURE)
        }
    }

    private fun startPickActivity() {
        val pickIntent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.addCategory(Intent.CATEGORY_OPENABLE)
        pickIntent.type = "image/*"
        if (pickIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivityForResult(pickIntent, RC_CHOOSE_PICTURE)
        }
    }

    private fun addToStorage(bitmap: Bitmap?) {
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val id = abs(Random.nextLong()).toString()

        val uploadTask = storageRef.child(id).putBytes(data)

        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
            return@Continuation storageRef.child(id).downloadUrl
        }).addOnSuccessListener { uri: Uri? ->
            Picasso.get().load(uri).into(view?.check_in_photo)
            this.imageUri = uri.toString()
        }
    }
}