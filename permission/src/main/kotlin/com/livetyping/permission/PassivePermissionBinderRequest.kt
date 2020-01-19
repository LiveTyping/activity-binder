package com.livetyping.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.livetyping.core.BinderRequest


class PassivePermissionBinderRequest(
    private val permission: String,
    private val block: (granted: Boolean) -> Unit
) :
    BinderRequest<Boolean>() {

    override fun request(activity: Activity) {
        if (isPermissionGranted(permission, activity)) {
            block(true)
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                block(false)
            } else ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            block(true)
        } else block(false)
    }

    @PermissionChecker.PermissionResult
    private fun isPermissionGranted(permission: String, activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PermissionChecker.PERMISSION_GRANTED
    }
}