package com.livetyping.permission.request

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.livetyping.core.BinderRequest


class MultiplyGeneralPermissionBinderRequest(
    private val permissions: Iterable<String>
) : BinderRequest<Map<String, Boolean>>() {

    private val result = mutableMapOf<String, Boolean>()

    override fun request(activity: Activity) {
        val groupedByGranted = permissions.groupBy {
            ContextCompat.checkSelfPermission(
                activity,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
        groupedByGranted[true]?.forEach { result[it] = true }
        val denied = groupedByGranted[false]
        if (denied == null) {
            block(result)
        } else {
            denied.toTypedArray().let {
                ActivityCompat.requestPermissions(activity, it, requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        activity: Activity,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissions.forEachIndexed { index, permission ->
            result[permission] = grantResults[index] == PackageManager.PERMISSION_GRANTED
        }
        block(result)
    }
}