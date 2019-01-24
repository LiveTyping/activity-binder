package com.livetyping.permission


import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED


internal abstract class PermissionRequest(protected val resultListener: (result:Boolean) -> Unit) {

    protected lateinit var permission: String
    protected var requestCode = 0

    internal fun needPermission(requestCode: Int, permission: String, activity: Activity) {
        this.requestCode = requestCode
        this.permission = permission
        concreteNeedPermission(requestCode, permission, activity)
    }

    protected abstract fun concreteNeedPermission(requestCode: Int, permission: String, activity: Activity)

    internal abstract fun afterSettingsActivityResult(requestCode: Int, data: Intent?, activity: Activity)

    internal abstract fun afterRequest(granted: Boolean, activity: Activity)

    protected fun checkPermission(permission: String, activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PERMISSION_GRANTED
    }
}
