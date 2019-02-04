package com.livetyping.permission


import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker.PERMISSION_DENIED
import android.support.v4.content.PermissionChecker.PERMISSION_GRANTED


internal abstract class PermissionRequest(protected val resultListener: (result: Boolean) -> Unit) {

    protected lateinit var permission: String
    protected var requestCode = 0
    protected lateinit var permissions: Iterable<String>

    internal fun needPermissions(requestCode: Int, permissions: Iterable<String>, activity: Activity) {
        this.requestCode = requestCode
        this.permissions = permissions
        bunchNeedPermissions(requestCode, permissions, activity)
    }

    protected abstract fun concreteNeedPermission(requestCode: Int, permission: String, activity: Activity)

    protected abstract fun bunchNeedPermissions(requestCode: Int, permissions: Iterable<String>, activity: Activity)

    internal abstract fun afterSettingsActivityResult(requestCode: Int, data: Intent?, activity: Activity)

    internal abstract fun afterRequest(granted: Boolean, activity: Activity)

    protected fun checkPermission(permission: String, activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PERMISSION_GRANTED
    }

    protected fun checkPermissionDenied(permission: String, activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PERMISSION_DENIED
    }

    protected fun checkPermissions(permissions: Iterable<String>, activity: Activity): Boolean {
        var isExistsUncheckedPermission = false
        permissions.iterator().forEach {
            isExistsUncheckedPermission = checkPermission(it, activity)
        }
        return isExistsUncheckedPermission
    }
}