package com.livetyping.permission.request

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.livetyping.permission.R


class ActivePermissionBinderRequest(
    private val permission: String,
    @StringRes private val rationaleTextStringRes: Int,
    @StringRes private val buttonOkTextStringRes: Int = android.R.string.ok,
    @StringRes private val buttonSettingsStringRes: Int = R.string.permission_settings_default_button_text
) :
    SinglePermissionBinderRequest<Boolean>() {

    private var rationaleShowed = false

    override fun request(activity: Activity) {
        if (isPermissionGranted(permission, activity)) {
            block(true)
        } else {
            if (shouldShowRationale(activity)) {
                rationaleShowed = true
                showRationaleDialog(activity, buttonOkTextStringRes) { requestPermission(activity) }
            } else {
                requestPermission(activity)
            }
        }
    }

    override fun onRequestPermissionsResult(
        activity: Activity,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            block(true)
        } else {
            if (rationaleShowed.not() && shouldShowRationale(activity)) {
                block(false)
            } else {
                if (rationaleShowed) {
                    block(false)
                } else {
                    showRationaleDialog(activity, buttonSettingsStringRes) { openAppSettings(activity) }
                }
            }
        }
    }

    private fun shouldShowRationale(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    private fun showRationaleDialog(activity: Activity, buttonResId: Int, buttonBlock: () -> Unit) {
        AlertDialog.Builder(activity)
            .setMessage(rationaleTextStringRes)
            .setPositiveButton(buttonResId) { _, _ -> buttonBlock() }
            .show()
    }

    private fun requestPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission),
            requestCode
        )
    }

    private fun openAppSettings(activity: Activity) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${activity.packageName}")
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        activity.startActivityForResult(intent, requestCode)
    }
}