package edu.rosehulman.gearlocker.inventory

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
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
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.synthetic.main.add_edit_management.view.*
import kotlinx.android.synthetic.main.add_item_confirmation.view.*
import java.io.ByteArrayOutputStream
import kotlin.math.abs
import kotlin.random.Random

class AddItem : Fragment() {

    private val RC_TAKE_PICTURE = 1
    private val RC_CHOOSE_PICTURE = 2

    private val itemCategoriesRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_ITEM_CATEGORIES)

    private val storageRef = FirebaseStorage.getInstance().reference.child("images")
    private var imageUri: String? = null

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.add_edit_management, container, false)
        val args: AddItemArgs by navArgs()
        activity?.findNavController(R.id.nav_host_fragment_management)?.graph?.startDestination =
            R.id.addItem
        view.upload_from_device.setOnClickListener {
            startPickActivity()
        }
        view.upload_from_camera.setOnClickListener {
            startCameraActivity()
        }
        if (!args.isAdd) {
            val item = args.item
            view.name_of_gear_edittext.setText(item!!.name)
            view.price_textview.setText(item.estimatedCost.toString())
            view.seekBar.progress = item.condition
            view.description_edittext.setText(item.description)
        }
        view.submit_button.setOnClickListener {
            try {
                val item = Item(
                    view.name_of_gear_edittext.text.toString(),
                    view.price_textview.text.toString().toFloat(),
                    view.seekBar.progress,
                    view.description_edittext.text.toString(),
                    view.category_spinner.selectedItem.toString(),
                    imageUri!!
                )
                args.itemInterface.onItemAdded(item)
                val alertView =
                    LayoutInflater.from(activity).inflate(R.layout.add_item_confirmation, null)
                val builderCreated = AlertDialog.Builder(activity).create()
                alertView.view_inventory.setOnClickListener {
                    findNavController().navigate(R.id.navigation_management_inventory)
                    builderCreated.dismiss()
                }
                if(args.isAdd) {
                    alertView.add_additional_item.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putParcelable("itemInterface", args.itemInterface)
                        findNavController().navigate(R.id.addItem)
                        builderCreated.dismiss()
                    }
                } else {
                    alertView.add_additional_item.isVisible = false
                }
                builderCreated.setView(alertView)
                builderCreated.show()
            } catch (e: Exception) {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Missing Input")
                builder.setMessage("Please review your item change or addition.")
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.create().show()
            }
        }

        itemCategoriesRef.get().addOnSuccessListener { snapshot ->
            val categories = snapshot.documents.map { it.get("name") as String }.sorted()
            view.category_spinner.adapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categories
            )
        }
        return view

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
            Picasso.get().load(uri).into(view?.gear_image)
            this.imageUri = uri.toString()
        }
    }
}