package com.livetyping.permission

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityCompat


internal class PassivePermissionRequest(resultListener: (permissionMap: HashMap<String, Boolean>) -> Unit)
    : PermissionRequest(resultListener) {

    override fun onPermissionsNeedDenied(activity: Activity) {
        val permissionsWithoutRationale = getPermissionsWithoutRationale(activity)
        if (!permissionsWithoutRationale.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsWithoutRationale.toTypedArray(), requestCode)
        } else {
            syncPermissionsGrantedResult(activity)
            resultListener.invoke(permissionHashMap)
        }
    }

    override fun afterRequest(activity: Activity) {
        syncPermissionsGrantedResult(activity)
        resultListener.invoke(permissionHashMap)
    }

    override fun afterSettingsActivityResult(requestCode: Int, data: Intent?, activity: Activity) {
        //nothing for this request
    }
}
