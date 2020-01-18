package com.livetyping.permission

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat


internal class ActivePermissionRequest(
        resultListener: (HashMap<String, Boolean>) -> Unit,
        private val settingsButtonText: String,
        private val rationaleText: String
) : PermissionRequest(resultListener) {

    private var rationaleShowed = false

    override fun onPermissionsNeedDenied(activity: Activity) {
        val permissionsWithoutRationale = getPermissionsWithoutRationale(activity).toList()
        if (permissionsWithoutRationale.isEmpty()) {
            showRequestPermissionDialog(activity)
            rationaleShowed = true
        } else {
            val deniedPermissions = getDeniedPermissions(activity)
            ActivityCompat.requestPermissions(activity, deniedPermissions.toTypedArray(), requestCode)
        }
    }

    override fun afterRequest(activity: Activity) {
        if (areAllPermissionGranted(activity)) {
            invokeResult(activity)
        } else {
            if (!rationaleShowed && getPermissionsWithoutRationale(activity).isNotEmpty()) {
                showOpenSettingsDialog(activity)
            } else {
                invokeResult(activity)
            }
        }
    }

    override fun afterSettingsActivityResult(requestCode: Int, data: Intent?, activity: Activity) {
        invokeResult(activity)
    }

    private fun showRequestPermissionDialog(activity: Activity) {
        AlertDialog.Builder(activity)
                .setMessage(rationaleText)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), requestCode)
                }
                .show()
    }

    private fun showOpenSettingsDialog(activity: Activity) {
        AlertDialog.Builder(activity)
                .setMessage(rationaleText)
                .setPositiveButton(settingsButtonText) { _, _ -> openAppSettings(activity) }
                .setNegativeButton(android.R.string.cancel) { _, _ -> invokeResult(activity) }
                .setOnCancelListener { invokeResult(activity) }
                .show()
    }

    private fun openAppSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:${activity.packageName}"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        activity.startActivityForResult(intent, requestCode)
    }
}
