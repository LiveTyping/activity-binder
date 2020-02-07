package com.livetyping.activitybinder

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.livetyping.core.NewBinder
import com.livetyping.permission.PermissionBinder
import com.livetyping.permission.request.ActivePermissionBinderRequest
import com.livetyping.permission.request.MultiplyGeneralPermissionBinderRequest
import com.livetyping.permission.request.PassivePermissionBinderRequest
import kotlinx.android.synthetic.main.activity_permissions.*


class PermissionExampleActivity : AppCompatActivity() {
    companion object {
        private const val TAG_SINGLE = "single"
        private const val TAG_MULTIPLY = "multiply"
    }

    private lateinit var permissionBinder: PermissionBinder
    private lateinit var newBinder: NewBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)
        permissionBinder = (application as BinderExampleApplication).permissionBinder
        newBinder = (application as BinderExampleApplication).newBinder
        handleButtonSinglePermissions()
        handleButtonMultiplyPermissions()
    }

    private fun handleButtonMultiplyPermissions() {
        multiply_passive.setOnClickListener {
            val request = MultiplyGeneralPermissionBinderRequest(
                listOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.USE_SIP
                )
            )
            newBinder.request(request) {
                for ((permission, isGranted) in it) {
                    Log.d(
                        "PERMISSION_BINDER: ",
                        "$permission is " + if (isGranted) "granted" else "denied"
                    )
                }
            }
        }

        val rationaleText = getString(R.string.active_permission_rationale_text)
        //cab be placed in active permission method as third parameter
        val settingsButtonText = getString(R.string.active_permission_rationale_button_text)
        multiply_active.setOnClickListener {
            permissionBinder.activePermission(listOf(Manifest.permission.SEND_SMS, Manifest.permission.RECORD_AUDIO), rationaleText) {
                for ((permission, isGranted) in it) {
                    handleOutputResults(isGranted, TAG_MULTIPLY, permission)
                }
            }
        }
        multiply_global.setOnClickListener {
            permissionBinder.globalPermission(listOf(Manifest.permission.BODY_SENSORS, Manifest.permission.READ_CALENDAR),
                    ShowGlobalExplanationActivity::class.java) {
                for ((permission, isGranted) in it) {
                    handleOutputResults(isGranted, TAG_MULTIPLY, permission)
                }
            }
        }
    }

    private fun handleButtonSinglePermissions() {
        single_passive.setOnClickListener {
            newBinder.request(
                PassivePermissionBinderRequest(Manifest.permission.READ_EXTERNAL_STORAGE)
            ) {
                handleOutputResults(it, TAG_SINGLE)
            }
        }

        single_active.setOnClickListener {
            newBinder.request(
                ActivePermissionBinderRequest(
                    Manifest.permission.CAMERA,
                    R.string.active_permission_rationale_text
                )
            ) {
                handleOutputResults(it, TAG_SINGLE)
            }
        }

        single_global.setOnClickListener {
            permissionBinder.globalPermission(Manifest.permission.USE_SIP,
                    ShowGlobalExplanationActivity::class.java) {
                handleOutputResults(it, TAG_SINGLE)
            }
        }
    }

    private fun handleOutputResults(isGranted: Boolean, tag: String, permission: String = "") {
        if (isGranted) granted(tag, permission) else denied(tag, permission)
    }

    override fun onStart() {
        super.onStart()
        permissionBinder.attach(this)
        newBinder.attach(this)
    }

    override fun onStop() {
        permissionBinder.detach(this)
        newBinder.detach(this)
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        permissionBinder.onActivityResult(requestCode, data, this)
        newBinder.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionBinder.onRequestPermissionResult(requestCode, grantResults)
        newBinder.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun granted(tag: String, permission: String = "") {
        Toast.makeText(this, "$permission was granted", Toast.LENGTH_SHORT).show()
    }

    private fun denied(tag: String, permission: String = "") {
        Toast.makeText(this, "$permission was denied", Toast.LENGTH_SHORT).show()
    }
}
