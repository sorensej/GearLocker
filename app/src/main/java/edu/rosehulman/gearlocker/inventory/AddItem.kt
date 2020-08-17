package edu.rosehulman.gearlocker.inventory

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import edu.rosehulman.gearlocker.CameraAndUploadUtils
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.synthetic.main.add_edit_management.view.*
import kotlinx.android.synthetic.main.add_item_confirmation.view.*

class AddItem : Fragment(), CameraAndUploadUtils.OnAddedToStorageListener {

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
            CameraAndUploadUtils.startPickActivity(this)
        }
        view.upload_from_camera.setOnClickListener {
            CameraAndUploadUtils.startCameraActivity(this)
        }
        if (!args.isAdd) {
            val item = args.item
            view.name_of_gear_edittext.setText(item!!.name)
            view.price_textview.setText(String.format("%.2f", item.estimatedCost))
            view.seekBar.progress = item.condition
            view.description_edittext.setText(item.description)
            Picasso.get().load(item.curPhotoPath).into(view.gear_image)
            imageUri = item.curPhotoPath
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
                if (args.isAdd) {
                    args.itemInterface.onItemAdded(item)
                } else {
                    item.id = args.item!!.id
                    args.itemInterface.onEditItem(item)
                }
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
                        bundle.putBoolean("isAdd", args.isAdd)
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
                builder.setMessage("Please review your item change or addition: $e")
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.create().show()
                e.printStackTrace()
            }
        }
        view.cancel_button.setOnClickListener {
            if (this.imageUri != null && args.isAdd) {
                val ref = FirebaseStorage.getInstance().getReferenceFromUrl(this.imageUri!!)
                ref.delete()
            }
            findNavController().navigate(R.id.navigation_management_inventory)
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
        Picasso.get().load(uriString).into(view?.gear_image)
        this.imageUri = uriString
    }
}