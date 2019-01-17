package com.livetyping.activitybinder

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.livetyping.permission.PermissionBinder
import kotlinx.android.synthetic.main.activity_permissions.*


class PermissionExampleActivity : AppCompatActivity() {

    private lateinit var permissionBinder: PermissionBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)
        permissionBinder = (application as BinderExampleApplication).permissionBinder

        passive.setOnClickListener {
            permissionBinder.passivePermission(Manifest.permission.CAMERA) { granted ->
                if (granted) granted() else denied()
            }
        }

        val rationaleText = getString(R.string.active_permission_rationale_text)
        //cab be placed in active permission method as third parameter
        val settingsButtonText = getString(R.string.active_permission_rationale_button_text)
        active.setOnClickListener {
            permissionBinder.activePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, rationaleText) { granted ->
                if (granted) granted() else denied()
            }
        }
        global.setOnClickListener {
            permissionBinder.globalPermission(Manifest.permission.SEND_SMS,
                    ShowGlobalExplanationActivity::class.java) { granted ->
                if (granted) granted() else denied()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        permissionBinder.attach(this)
    }

    override fun onStop() {
        permissionBinder.detach(this)
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        permissionBinder.onActivityResult(requestCode, data, this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionBinder.onRequestPermissionResult(requestCode, grantResults)
    }

    private fun granted() {
        Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show()
    }

    private fun denied() {
        Toast.makeText(this, "denied", Toast.LENGTH_SHORT).show()
    }
}
