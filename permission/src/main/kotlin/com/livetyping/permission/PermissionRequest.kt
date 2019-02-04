package com.livetyping.permission


import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker.PERMISSION_DENIED
import android.support.v4.content.PermissionChecker.PERMISSION_GRANTED


internal abstract class PermissionRequest(protected val resultListener: (result: Boolean) -> Unit) {

    protected var requestCode = 0
    protected lateinit var permissions: MutableList<String>

    internal fun needPermissions(requestCode: Int, permissions: Iterable<String>, activity: Activity) {
        this.requestCode = requestCode
        this.permissions = permissions.toMutableList()
        bunchNeedPermissions(requestCode, activity)
    }

    fun bunchNeedPermissions(requestCode: Int, activity: Activity){
        if (areAllPermissionGranted(activity)) {
            resultListener.invoke(true)
        } else {
            onPermissionsNeedDenied(activity)
        }
    }
    internal abstract fun onPermissionsNeedDenied(activity: Activity)

    internal abstract fun afterSettingsActivityResult(requestCode: Int, data: Intent?, activity: Activity)

    internal abstract fun afterRequest(granted: Boolean, activity: Activity)

    private fun isPermissionGranted(permission: String, activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PERMISSION_GRANTED
    }

    protected fun isPermissionDenied(permission: String, activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PERMISSION_DENIED
    }

    protected fun getPermissionsWithoutRationale(activity: Activity): List<String> {
        return permissions.filter {
            !ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }
    }

    protected fun getDeniedPermissions(activity: Activity): List<String> {
        return permissions.filter { isPermissionDenied(it, activity) }
    }

    protected fun getGrantedPermissions(activity: Activity): List<String> {
        return permissions.filter { isPermissionGranted(it, activity) }.toMutableList()
    }

    protected fun areAllPermissionGranted(activity: Activity): Boolean {
        permissions.forEach {
            if (!isPermissionGranted(it, activity)) return@areAllPermissionGranted false
        }
        return true
    }
}