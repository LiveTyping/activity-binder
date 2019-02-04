package com.livetyping.activitybinder

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.livetyping.images.test.TestImageBinder
import com.livetyping.images.test.impl.PhotoRequestCachePath
import com.livetyping.images.test.impl.PhotoRequestDefaultPath
import com.livetyping.images.test.impl.GalleryMultipleRequest
import com.livetyping.images.test.impl.GallerySingleRequest
import com.livetyping.permission.PermissionBinder
import kotlinx.android.synthetic.main.activity_new_binder.*


class NewBinderActivity : AppCompatActivity() {

    private lateinit var imagesBinder: TestImageBinder
    private lateinit var permissionBinder: PermissionBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_binder)
        val binderExampleApplication = application as BinderExampleApplication
        imagesBinder = binderExampleApplication.testImagesBinder
        permissionBinder = binderExampleApplication.permissionBinder

        multiple_gallery.setOnClickListener {
            imagesBinder.requestPhoto(GalleryMultipleRequest { files ->
                image.setImageURI(Uri.fromFile(files[0]))
                Toast.makeText(this, files.size.toString(), Toast.LENGTH_SHORT).show()
            })
        }

        single_gallery.setOnClickListener {
            imagesBinder.requestPhoto(GallerySingleRequest { file ->
                image.setImageURI(Uri.fromFile(file))
            })
        }

        single_request_chooser.setOnClickListener {
            imagesBinder.requestPhoto(GallerySingleRequest("select file") { file ->
                image.setImageURI(Uri.fromFile(file))
            })
        }

        default_photo.setOnClickListener {
            permissionBinder.passivePermission(Manifest.permission.CAMERA) {
                imagesBinder.requestPhoto(PhotoRequestDefaultPath { file ->
                    image.setImageURI(Uri.fromFile(file))
                })
            }
        }

        cahche_path_photo.setOnClickListener {
            permissionBinder.passivePermission(Manifest.permission.CAMERA) {
                imagesBinder.requestPhoto(PhotoRequestCachePath("cache_files") { file ->
                    image.setImageURI(Uri.fromFile(file))
                })
            }
        }

    }

    override fun onStart() {
        super.onStart()
        imagesBinder.attach(this)
        permissionBinder.attach(this)
    }

    override fun onStop() {
        super.onStop()
        imagesBinder.detach(this)
        permissionBinder.detach(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagesBinder.onActivityResult(requestCode, resultCode, data, this)
        permissionBinder.onActivityResult(requestCode, data, this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagesBinder.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionBinder.onRequestPermissionResult(requestCode, grantResults)
    }
}
