package com.livetyping.permission.request

import android.app.Activity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.livetyping.core.BinderRequest


abstract class SinglePermissionBinderRequest<T> : BinderRequest<T>() {

    @PermissionChecker.PermissionResult
    protected fun isPermissionGranted(permission: String, activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PermissionChecker.PERMISSION_GRANTED
    }
}