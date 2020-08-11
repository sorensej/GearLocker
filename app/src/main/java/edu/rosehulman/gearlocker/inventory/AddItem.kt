package edu.rosehulman.gearlocker.inventory

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.synthetic.main.add_edit_management.*
import kotlinx.android.synthetic.main.add_edit_management.view.*
import kotlinx.android.synthetic.main.add_item_confirmation.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddItem: Fragment() {

    private val RC_TAKE_PICTURE = 1
    private val RC_CHOOSE_PICTURE = 2

    private var currentPhotoPath = ""
    private var itemView: ImageView? = null

    private val itemCategoriesRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.FB_ITEM_CATEGORIES)

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.add_edit_management, container, false)
        activity?.findNavController(R.id.nav_host_fragment_management)?.graph?.startDestination = R.id.addItem
        val args: AddItemArgs by navArgs()
        itemView = view.gear_image
        view.upload_from_device.setOnClickListener {
            Log.d(Constants.TAG, "Upload from device")
            launchChooseIntent()
            Picasso.get()
                .load(currentPhotoPath.toUri())
                .into(view.gear_image)
        }
        view.upload_from_camera.setOnClickListener {
            launchCameraIntent()
            Picasso.get().load(currentPhotoPath).into(view.gear_image)
            view.refreshDrawableState()
        }
        view.submit_button.setOnClickListener {
            var item = Item(view.name_of_gear_edittext.text.toString(),
            view.quantity_edittext.text.toString().toFloat(),
            view.seekBar.progress,view.description_edittext.text.toString(),view.category_spinner.selectedItem.toString(), currentPhotoPath)
            (args.inventoryFragment as InventoryAdapter.ItemInterface).onItemAdded(item)
            var alertView = LayoutInflater.from(activity).inflate(R.layout.add_item_confirmation, null)
            var builderCreated = AlertDialog.Builder(activity).create()
            alertView.view_inventory.setOnClickListener {
                findNavController().navigate(R.id.navigation_management_inventory)
                builderCreated.dismiss()
            }
            alertView.add_additional_item.setOnClickListener {
                findNavController().navigate(R.id.addItem)
                builderCreated.dismiss()
            }
            builderCreated.setView(alertView)
            builderCreated.show()
        }

        itemCategoriesRef.get().addOnSuccessListener { snapshot ->
            val categories = snapshot.documents.map { it.get("name") as String }.sorted()
            view.category_spinner.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        }

        return view
    }
    //catch and kit below
    private fun launchCameraIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Log.e(Constants.TAG, "$ex")
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    // authority declared in manifest
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "edu.rosehulman.gearlocker",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, RC_TAKE_PICTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun launchChooseIntent() {
        // https://developer.android.com/guide/topics/providers/document-provider
        val choosePictureIntent = Intent(
            Intent.ACTION_OPEN_DOCUMENT,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        choosePictureIntent.addCategory(Intent.CATEGORY_OPENABLE)
        choosePictureIntent.type = "image/*"
        if (choosePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivityForResult(choosePictureIntent, RC_CHOOSE_PICTURE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_TAKE_PICTURE -> {
                    sendCameraPhotoToAdapter()
                }
                RC_CHOOSE_PICTURE -> {
                    sendGalleryPhotoToAdapter(data)
                }
            }
            itemView?.setImageURI(currentPhotoPath.toUri())
        }
    }

    private fun sendCameraPhotoToAdapter() {
        addPhotoToGallery()
        Log.d(Constants.TAG, "Sending to adapter this photo: $currentPhotoPath")
    }

    private fun sendGalleryPhotoToAdapter(data: Intent?) {
        if (data != null && data.data != null) {
            currentPhotoPath = data.data!!.toString()
        }
    }

    // Works Not working on phone
    private fun addPhotoToGallery() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            requireActivity().sendBroadcast(mediaScanIntent)
        }
    }
}