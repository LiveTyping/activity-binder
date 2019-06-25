package com.livetyping.permission

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat


internal class GlobalPermissionRequest(
        private val preSettingsClass: Class<out PreSettingsActivity>,
        resultListener: (HashMap<String, Boolean>) -> Unit
) : PermissionRequest(resultListener) {

    companion object {
        internal const val PERMISSION_REQUEST_CODE_KEY = "PermissionRepository.PermissionCodeKey"
        internal const val PERMISSION_KEY = "PermissionRepository.PermissionKey"
    }


    override fun onPermissionsNeedDenied(activity: Activity) {
        ActivityCompat.requestPermissions(activity, permissions.toList().toTypedArray(), requestCode)
    }


    override fun afterRequest(activity: Activity) {
        if (areAllPermissionGranted(activity)) {
            invokeResult(activity)
        } else {
            val intent = Intent(activity, preSettingsClass)
            with(intent) {
                putExtra(PERMISSION_KEY, permissions.toTypedArray())
                putExtra(PERMISSION_REQUEST_CODE_KEY, requestCode)
            }
            activity.startActivityForResult(intent, requestCode)
        }
    }

    override fun afterSettingsActivityResult(requestCode: Int, data: Intent?, activity: Activity) {
        bunchNeedPermissions(activity)
    }
}
