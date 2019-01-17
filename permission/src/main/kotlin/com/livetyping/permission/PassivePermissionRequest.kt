package com.livetyping.permission

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityCompat


internal class PassivePermissionRequest(resultListener: (result: Boolean) -> Unit)
    : PermissionRequest(resultListener) {


    override fun concreteNeedPermission(requestCode: Int, permission: String, activity: Activity) {
        val checkPermission = checkPermission(permission, activity)
        if (checkPermission) {
            resultListener.invoke(true)
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                resultListener.invoke(false)
            } else ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)

        }
    }

    override fun afterRequest(granted: Boolean, activity: Activity) {
        resultListener.invoke(granted)
    }

    override fun afterSettingsActivityResult(requestCode: Int, data: Intent?, activity: Activity) {
        //nothing for this request
    }
}
