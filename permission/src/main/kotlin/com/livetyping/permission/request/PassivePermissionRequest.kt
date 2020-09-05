package com.livetyping.permission.request

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


class PassivePermissionRequest(
    private val permission: String
) :
    SinglePermissionBinderRequest<Boolean>() {

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
        activity: Activity,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            block(true)
        } else block(false)
    }


}