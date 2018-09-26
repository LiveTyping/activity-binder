package com.livetyping.permission

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat


internal class GlobalPermissionRequest(
        private val clazz: Class<out PreSettingsActivity>,
        onGranted: () -> Unit)
    : PermissionRequest(onGranted, null) {

    companion object {
        internal val PERMISSION_REQUEST_CODE_KEY = "PermissionRepository.PermissionCodeKey"
        internal val PERMISSION_KEY = "PermissionRepository.PermissionKey"
    }

    override fun concreteNeedPermission(requestCode: Int, permission: String, activity: Activity) {
        val checkPermission = checkPermission(permission, activity)
        if (checkPermission) {
            onGranted()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        }
    }

    override fun afterRequest(granted: Boolean, activity: Activity) {
        if (granted) {
            onGranted()
        } else {
            val intent = Intent(activity, clazz)
            with(intent) {
                putExtra(PERMISSION_KEY, permission)
                putExtra(PERMISSION_REQUEST_CODE_KEY, requestCode)
            }
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun afterSettingsActivityResult(requestCode: Int, data: Intent?, activity: Activity) {
        concreteNeedPermission(requestCode, permission, activity)
    }
}