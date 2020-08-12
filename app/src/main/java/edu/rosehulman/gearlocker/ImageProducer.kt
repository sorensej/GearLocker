package edu.rosehulman.gearlocker
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat.requireContext

import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

//catch and kit below
class ImageProducer(val fragment: Fragment) {

    private val RC_TAKE_PICTURE = 1
    private val RC_CHOOSE_PICTURE = 2

    var currentPhotoPath = ""

    fun launchCameraIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(fragment.requireActivity().packageManager)?.also {
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
                        fragment.requireContext(),
                        "edu.rosehulman.gearlocker",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    fragment.startActivityForResult(takePictureIntent, RC_TAKE_PICTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File = fragment.requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun launchChooseIntent() {
        // https://developer.android.com/guide/topics/providers/document-provider
        val choosePictureIntent = Intent(
            Intent.ACTION_OPEN_DOCUMENT,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        choosePictureIntent.addCategory(Intent.CATEGORY_OPENABLE)
        choosePictureIntent.type = "image/*"
        if (choosePictureIntent.resolveActivity(fragment.requireContext().packageManager) != null) {
            fragment.startActivityForResult(choosePictureIntent, RC_CHOOSE_PICTURE)
        }
    }


    fun sendCameraPhotoToAdapter() {
        addPhotoToGallery()
    }

    fun sendGalleryPhotoToAdapter(data: Intent?) {
        if (data != null && data.data != null) {
            currentPhotoPath = data.data!!.toString()
        }
    }

    // Works Not working on phone
    private fun addPhotoToGallery() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            fragment.requireActivity().sendBroadcast(mediaScanIntent)
        }
    }
}