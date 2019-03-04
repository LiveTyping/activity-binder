package com.livetyping.permission


import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.PERMISSION_DENIED

internal abstract class PermissionRequest(protected val resultListener: (HashMap<String, Boolean>) -> Unit) {
    protected val permissionHashMap = HashMap<String, Boolean>()
    protected var requestCode = 0
    protected lateinit var permissions: MutableList<String>

    internal fun needPermissions(requestCode: Int, permissions: Iterable<String>, activity: Activity) {
        this.requestCode = requestCode
        this.permissions = permissions.toMutableList()
        initPermissionHashMap()
        bunchNeedPermissions(activity)
    }

    private fun initPermissionHashMap() {
        permissionHashMap.clear()
        permissions.forEach {
            permissionHashMap[it] = false
        }
    }

    fun bunchNeedPermissions(activity: Activity) {
        if (areAllPermissionGranted(activity)) {
            syncPermissionsGrantedResult(activity)
            resultListener.invoke(permissionHashMap)
        } else {
            onPermissionsNeedDenied(activity)
        }
    }

    internal abstract fun onPermissionsNeedDenied(activity: Activity)

    internal abstract fun afterSettingsActivityResult(requestCode: Int, data: Intent?, activity: Activity)

    internal abstract fun afterRequest(activity: Activity)

    @PermissionChecker.PermissionResult
    private fun isPermissionGranted(permission: String, activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PERMISSION_GRANTED
    }

    @PermissionChecker.PermissionResult
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


    protected fun syncPermissionsGrantedResult(activity: Activity) {
        for ((permission) in permissionHashMap) {
            permissionHashMap[permission] = isPermissionGranted(permission, activity)
        }
    }

    protected fun invokeResult(activity: Activity) {
        syncPermissionsGrantedResult(activity)
        resultListener.invoke(permissionHashMap)
    }

    @PermissionChecker.PermissionResult
    protected fun areAllPermissionGranted(activity: Activity): Boolean {
        permissions.forEach {
            if (!isPermissionGranted(it, activity)) return@areAllPermissionGranted false
        }
        return true
    }
}

