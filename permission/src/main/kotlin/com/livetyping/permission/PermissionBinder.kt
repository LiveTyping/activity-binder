package com.livetyping.permission

import android.app.Activity
import android.content.Intent
import android.support.annotation.StringRes
import android.support.v4.content.PermissionChecker


class PermissionBinder : Binder() {
    private val requests: MutableMap<Int, PermissionRequest> = mutableMapOf()
    private val permissionCodes: PermissionRequestCodes = PermissionRequestCodes()

    fun passivePermission(permission: String, onGranted: () -> Unit, onDenied: () -> Unit) {
        needPermission(permission, PassivePermissionRequest(onGranted, onDenied))
    }

    fun activePermission(permission: String,
                         rationaleText: String,
                         @StringRes settingsButtonStringRes: Int,
                         onGranted: () -> Unit,
                         onDenied: () -> Unit) {
        needPermission(permission, ActivePermissionRequest(onGranted, onDenied, settingsButtonStringRes, rationaleText))
    }

    fun globalPermission(permission: String, clazz: Class<out PreSettingsActivity>, onGranted: () -> Unit) {
        needPermission(permission, GlobalPermissionRequest(clazz, onGranted))
    }

    fun onRequestPermissionResult(code: Int, grantResults: IntArray) {
        val granted = grantResults[0] == PermissionChecker.PERMISSION_GRANTED
        val requester = requests[code]
        getAttachedObject()
                ?: throw IllegalStateException("PermissionRepository. Haven't attached activity")
        requester?.afterRequest(granted, getAttachedObject()!!)
    }

    fun onActivityResult(requestCode: Int, data: Intent?, activity: Activity) {
        requests[requestCode]?.afterSettingsActivityResult(requestCode, data, activity)
    }


    private fun needPermission(permission: String, request: PermissionRequest) {
        getAttachedObject()
                ?: throw IllegalStateException("PermissionRepository. Haven't attached activity")
        val requestCode = permissionCodes.getCode(permission)
        requests.put(requestCode, request)
        requests[requestCode]!!.needPermission(requestCode, permission, getAttachedObject()!!)
    }

}