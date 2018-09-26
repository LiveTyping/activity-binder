package com.livetyping.permission

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat


internal class PassivePermissionRequest(onGranted: () -> Unit, onDenied: () -> Unit)
    : PermissionRequest(onGranted, onDenied) {



    override fun concreteNeedPermission(requestCode: Int, permission: String, activity: Activity) {
        val checkPermission = checkPermission(permission, activity)
        if (checkPermission) {
            onGranted()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                onDenied?.invoke()
            } else ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)

        }
    }

    override fun afterRequest(granted: Boolean, activity: Activity) {
        if (granted) {
            onGranted()
        } else onDenied?.invoke()
    }

    override fun afterSettingsActivityResult(requestCode: Int, data: Intent?, activity: Activity) {
        //nothing for this request
    }
}