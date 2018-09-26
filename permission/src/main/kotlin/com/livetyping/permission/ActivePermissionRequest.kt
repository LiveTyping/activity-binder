package com.livetyping.permission

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.annotation.StringRes
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog


internal class ActivePermissionRequest(onGranted: () -> Unit,
                                       onDenied: () -> Unit,
                                       @StringRes val settingsButtonStringRes: Int,
                                       private val rationaleText: String)
    : PermissionRequest(onGranted, onDenied) {

    private var rationaleShowed = false

    override fun concreteNeedPermission(requestCode: Int, permission: String, activity: Activity) {
        val checkPermission = checkPermission(permission, activity)
        if (checkPermission) {
            onGranted()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                AlertDialog.Builder(activity)
                        .setMessage(rationaleText)
                        .setPositiveButton(android.R.string.ok) { dialog, which ->
                            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
                        }.create().show()
                rationaleShowed = true
            } else ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        }
    }

    override fun afterRequest(granted: Boolean, activity: Activity) {
        if (granted) {
            onGranted()
        } else {
            if (!rationaleShowed && !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                AlertDialog.Builder(activity)
                        .setMessage(rationaleText)
                        .setPositiveButton(settingsButtonStringRes) { dialog, which ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + activity.packageName))
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                            activity.startActivityForResult(intent, requestCode)
                        }.setNegativeButton(android.R.string.cancel) { dialog, which -> onDenied?.invoke() }
                        .setOnCancelListener { onDenied?.invoke() }
                        .create().show()
            } else {
                onDenied?.invoke()
            }
        }
    }

    override fun afterSettingsActivityResult(requestCode: Int, data: Intent?, activity: Activity) {
        val checkPermission = checkPermission(permission, activity)
        if (checkPermission) onGranted() else onDenied?.invoke()
    }
}