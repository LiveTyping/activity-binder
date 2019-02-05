package com.livetyping.activitybinder

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.livetyping.permission.PermissionBinder
import kotlinx.android.synthetic.main.activity_permissions.*


class PermissionExampleActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "acivity-binder result"
    }
    private lateinit var permissionBinder: PermissionBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)
        permissionBinder = (application as BinderExampleApplication).permissionBinder

        passive.setOnClickListener {
            permissionBinder.passivePermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
                if (it) {
                    Log.i("HUI", "granted")
                } else {
                    Log.i("HUI", "denied")
                }
            }
        }

        val rationaleText = getString(R.string.active_permission_rationale_text)
        //cab be placed in active permission method as third parameter
        val settingsButtonText = getString(R.string.active_permission_rationale_button_text)
        active.setOnClickListener {
            permissionBinder.activePermission(Manifest.permission.CAMERA, rationaleText) {
              /*  for ((permission, grantedResult) in it) {*/
                    if (it) {
                        Log.i("HUI", "granted")
                    } else {
                        Log.i("HUI", "denied")
                    }
               // }
            }
        }
        global.setOnClickListener {
            permissionBinder.globalPermission(arrayListOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                    ShowGlobalExplanationActivity::class.java) {
                for ((permission, grantedResult) in it) {
                    if (grantedResult) {
                        Log.i("HUI", permission + "granted")
                    } else {
                        Log.i("HUI", permission + "denied")
                    }
                }
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