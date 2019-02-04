package com.livetyping.permission

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat


internal class PassivePermissionRequest(resultListener: (result: Boolean) -> Unit)
    : PermissionRequest(resultListener) {

    override fun onPermissionsNeedDenied(activity: Activity) {
        val permissionsWithoutRationale = getPermissionsWithoutRationale(activity)
        if (!permissionsWithoutRationale.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsWithoutRationale.toTypedArray(), requestCode)
        } else {
            resultListener.invoke(false)
        }
    }

    override fun afterRequest(granted: Boolean, activity: Activity) {
        resultListener.invoke(granted)
    }

    override fun afterSettingsActivityResult(requestCode: Int, data: Intent?, activity: Activity) {
        //nothing for this request
    }
}