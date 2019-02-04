package com.livetyping.permission

import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.util.Log


internal class PassivePermissionRequest(resultListener: (result: Boolean) -> Unit)
    : PermissionRequest(resultListener) {


    override fun concreteNeedPermission(requestCode: Int, permission: String, activity: Activity) {
        bunchNeedPermissions(requestCode, arrayListOf(permission), activity)
    }

    override fun bunchNeedPermissions(requestCode: Int, permissions: Iterable<String>, activity: Activity) {
        if (checkPermissions(permissions, activity)) {
            resultListener.invoke(true)
        } else {
            val permissionList = permissions.toMutableList().filter { !ActivityCompat.shouldShowRequestPermissionRationale(activity, it) }
            if (!permissionList.isEmpty())
                ActivityCompat.requestPermissions(activity, permissionList.toList().toTypedArray(), requestCode)
            else resultListener.invoke(false)
        }
    }

    override fun afterRequest(granted: Boolean, activity: Activity) {
        resultListener.invoke(granted)
    }

    override fun afterSettingsActivityResult(requestCode: Int, data: Intent?, activity: Activity) {
        //nothing for this request
    }
}