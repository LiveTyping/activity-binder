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
            permissionBinder.passivePermission(Manifest.permission.CAMERA, { granted() }, { denied() })
        }
        active.setOnClickListener {
            permissionBinder.activePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.active_permission_rationale_text),
                    R.string.active_permission_rationale_button_text, { granted() }, { denied() })
        }
        global.setOnClickListener {
            permissionBinder.globalPermission(Manifest.permission.SEND_SMS,
                    ShowGlobalExplanationActivity::class.java) {
                granted()
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