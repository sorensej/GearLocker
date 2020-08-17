package edu.rosehulman.gearlocker

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import kotlin.math.abs
import kotlin.random.Random

object CameraAndUploadUtils {

    val RC_TAKE_PICTURE = 1
    val RC_CHOOSE_PICTURE = 2

    fun startCameraActivity(fragment: Fragment) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(fragment.requireActivity().packageManager) != null) {
            fragment.startActivityForResult(cameraIntent, RC_TAKE_PICTURE)
        }
    }

    fun startPickActivity(fragment: Fragment) {
        val pickIntent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.addCategory(Intent.CATEGORY_OPENABLE)
        pickIntent.type = "image/*"
        if (pickIntent.resolveActivity(fragment.requireActivity().packageManager) != null) {
            fragment.startActivityForResult(pickIntent, RC_CHOOSE_PICTURE)
        }
    }

    fun addToStorage(ref: StorageReference, bitmap: Bitmap?, oldUri: String?, callback: OnAddedToStorageListener) {
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val id = abs(Random.nextLong()).toString()

        val uploadTask = ref.child(id).putBytes(data)

        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
            return@Continuation ref.child(id).downloadUrl
        }).addOnSuccessListener { uri: Uri? ->

            if (oldUri != null) {
                FirebaseStorage
                    .getInstance()
                    .getReferenceFromUrl(oldUri)
                    .delete()
            }
            callback.onAddedToStorage(uri.toString())
        }
    }

    interface OnAddedToStorageListener {
        fun onAddedToStorage(uriString: String)
    }
}