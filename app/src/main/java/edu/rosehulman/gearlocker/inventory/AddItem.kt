package edu.rosehulman.gearlocker.inventory

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.ImageProducer
import edu.rosehulman.gearlocker.R
import edu.rosehulman.gearlocker.models.Item
import kotlinx.android.synthetic.main.add_edit_management.view.*
import kotlinx.android.synthetic.main.add_item_confirmation.view.*

class AddItem : Fragment() {

    private val RC_TAKE_PICTURE = 1
    private val RC_CHOOSE_PICTURE = 2

    private val imageProducer = ImageProducer(this)

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
        val args: AddItemArgs by navArgs()
        activity?.findNavController(R.id.nav_host_fragment_management)?.graph?.startDestination =
            R.id.addItem
        view.upload_from_device.setOnClickListener {
            imageProducer.launchChooseIntent()
            addAndShowImage(view, imageProducer)
        }
        view.upload_from_camera.setOnClickListener {
            imageProducer.launchCameraIntent()
            addAndShowImage(view, imageProducer)
        }
        if (!args.isAdd) {
            val item = args.item
            view.name_of_gear_edittext.setText(item!!.name)
            view.price_textview.setText(item!!.estimatedCost.toString())
            view.seekBar.progress = item!!.condition
            view.description_edittext.setText(item!!.description)
        }
        view.submit_button.setOnClickListener {
            try {
                val item = Item(
                    view.name_of_gear_edittext.text.toString(),
                    view.price_textview.text.toString().toFloat(),
                    view.seekBar.progress,
                    view.description_edittext.text.toString(),
                    view.category_spinner.selectedItem.toString(),
                    imageProducer.currentPhotoPath
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
                builder.setPositiveButton("OK") { dialog, which ->
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

    private fun addAndShowImage(view: View, imageProducer: ImageProducer) {
        Log.d(Constants.TAG, "Add image: ${imageProducer.currentPhotoPath}")
        imageProducer.add(imageProducer.currentPhotoPath)
        Log.d(Constants.TAG, "DownloadUri: ${imageProducer.downloadUri}")
        Picasso.get().load(imageProducer.downloadUri).into(view.gear_image)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_TAKE_PICTURE   -> {
                    imageProducer.sendCameraPhotoToAdapter()
                }
                RC_CHOOSE_PICTURE -> {
                    imageProducer.sendGalleryPhotoToAdapter(data)
                }
            }
            view?.gear_image?.setImageURI(imageProducer.currentPhotoPath.toUri())
        }
    }
}