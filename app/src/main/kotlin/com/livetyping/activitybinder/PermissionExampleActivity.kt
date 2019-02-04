package com.livetyping.activitybinder

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.livetyping.permission.PermissionBinder
import kotlinx.android.synthetic.main.activity_permissions.*


class PermissionExampleActivity : AppCompatActivity() {

    private lateinit var permissionBinder: PermissionBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)
        permissionBinder = (application as BinderExampleApplication).permissionBinder
        
        passive.setOnClickListener {
            permissionBinder.passivePermission(arrayListOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)) { granted ->
                if (granted) granted() else denied()
            }
        }

        val rationaleText = getString(R.string.active_permission_rationale_text)
        //cab be placed in active permission method as third parameter
        val settingsButtonText = getString(R.string.active_permission_rationale_button_text)
        active.setOnClickListener {
            permissionBinder.activePermission(arrayListOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), rationaleText) { granted ->
                if (granted) granted() else denied()
            }
        }
        global.setOnClickListener {
            permissionBinder.globalPermission(arrayListOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
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
        permissionBinder.onRequestPermissionResult(requestCode, permissions, grantResults)
    }

    private fun granted() {
        Toast.makeText(this, "granted", Toast.LENGTH_SHORT).show()
    }

    private fun denied() {
        Toast.makeText(this, "denied", Toast.LENGTH_SHORT).show()
    }
}