package com.livetyping.permission

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.annotation.StringRes
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog


internal class ActivePermissionRequest(resultListener: (result: Boolean) -> Unit,
                                       @StringRes val settingsButtonText: String,
                                       private val rationaleText: String)
    : PermissionRequest(resultListener) {

    private var rationaleShowed = false

    override fun onPermissionsNeedDenied(activity: Activity) {
        val permissionsWithoutRationale = getPermissionsWithoutRationale(activity).toList()
        if (permissionsWithoutRationale.isEmpty()) {
            AlertDialog.Builder(activity)
                    .setMessage(rationaleText)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), requestCode)
                    }.create().show()
            rationaleShowed = true
        } else {
            val deniedPermissions = getDeniedPermissions(activity)
            ActivityCompat.requestPermissions(activity, deniedPermissions.toTypedArray(), requestCode)
        }
    }


    override fun afterRequest(granted: Boolean, activity: Activity) {
        if (areAllPermissionGranted(activity)) {
            resultListener.invoke(true)
        } else {
            if (!rationaleShowed) {
                AlertDialog.Builder(activity)
                        .setMessage(rationaleText)
                        .setPositiveButton(settingsButtonText) { _, _ ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + activity.packageName))
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                            activity.startActivityForResult(intent, requestCode)
                        }.setNegativeButton(android.R.string.cancel) { _, _ -> resultListener.invoke(false) }
                        .setOnCancelListener { resultListener.invoke(false) }
                        .create().show()
            } else {
                resultListener.invoke(false)
            }
        }
    }

    override fun afterSettingsActivityResult(requestCode: Int, data: Intent?, activity: Activity) {
        val permissionWithoutRationale = getPermissionsWithoutRationale(activity)
        resultListener.invoke(permissionWithoutRationale.isEmpty())
    }
}