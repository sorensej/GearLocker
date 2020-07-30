package edu.rosehulman.gearlocker.managment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import edu.rosehulman.gearlocker.Constants
import edu.rosehulman.gearlocker.R


class ReturnToDashAlertFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = "Log out of management to return to dash?"
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
        Log.d(Constants.TAG, "Activity: ${activity?.supportFragmentManager?.popBackStack()}")
        alertDialogBuilder.setTitle(title)

        alertDialogBuilder.setPositiveButton("OK"
        ) { _, _ ->
            findNavController().navigate(R.id.navigation_management_dashboard)
        }
        alertDialogBuilder.setNegativeButton("Cancel"
        ) { dialog, _ ->
            dialog?.dismiss()
        }
        return alertDialogBuilder.create()
    }
}