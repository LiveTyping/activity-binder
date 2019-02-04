package com.livetyping.permission

import android.app.Activity
import android.content.Intent
import android.support.annotation.StringRes
import android.support.v4.content.PermissionChecker
import com.livetyping.core.Binder


class PermissionBinder : Binder() {
    private val requests: MutableMap<Int, PermissionRequest> = mutableMapOf()
    private val permissionCodes: PermissionRequestCodes = PermissionRequestCodes()


    fun passivePermission(permission: String, resultListener: (result: Boolean) -> Unit) {
        needPermissions(listOf(permission), PassivePermissionRequest(resultListener))
    }

    fun passivePermission(permissions: Iterable<String>, resultListener: (result: Boolean) -> Unit) {
        needPermissions(permissions, PassivePermissionRequest(resultListener))
    }

    fun activePermission(permission: String,
                         rationaleText: String,
                         @StringRes settingsButtonText: String = "settings",
                         resultListener: (result: Boolean) -> Unit) {
        needPermissions(listOf(permission), ActivePermissionRequest(resultListener, settingsButtonText, rationaleText))
    }

    fun activePermission(permissions: Iterable<String>,
                         rationaleText: String,
                         @StringRes settingsButtonText: String = "settings",
                         resultListener: (result: Boolean) -> Unit) {
        needPermissions(permissions, ActivePermissionRequest(resultListener, settingsButtonText, rationaleText))
    }

    fun globalPermission(permission: String, clazz: Class<out PreSettingsActivity>, resultListener: (result: Boolean) -> Unit) {
        needPermissions(listOf(permission), GlobalPermissionRequest(clazz, resultListener))
    }

    fun globalPermission(permissions: Iterable<String>, clazz: Class<out PreSettingsActivity>, resultListener: (result: Boolean) -> Unit) {
        needPermissions(permissions, GlobalPermissionRequest(clazz, resultListener))
    }

    fun onRequestPermissionResult(code: Int, permissions: Array<out String>, grantResults: IntArray) {
        val granted = grantResults.isNotEmpty() && grantResults[0] == PermissionChecker.PERMISSION_GRANTED
        val requester = requests[code]
        getAttachedObject()
                ?: throw IllegalStateException("PermissionRepository. Haven't attached activity")
        requester?.afterRequest(granted, getAttachedObject()!!)
    }

    fun onActivityResult(requestCode: Int, data: Intent?, activity: Activity) {
        requests[requestCode]?.afterSettingsActivityResult(requestCode, data, activity)
    }


    private fun needPermissions(permissions: Iterable<String>, request: PermissionRequest) {
        getAttachedObject()
                ?: throw IllegalStateException("PermissionRepository. Haven't attached activity")
        val requestCode = calculateRequestCode(permissions)
        requests[requestCode] = request
        request.needPermissions(requestCode, permissions, getAttachedObject()!!)
    }

    private fun calculateRequestCode(permissions: Iterable<String>): Int {
        return if (permissions.toList().size == 1) {
            permissionCodes.getCode(permissions.iterator().next())
        } else {
            PermissionRequestCodes.MULTIPLE_PERMISSIONS_CODE
        }
    }
}
