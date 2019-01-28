package com.livetyping.activitybinder

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.livetyping.images.ImagesBinder
import com.livetyping.images.settings.FilesPathSettings
import com.livetyping.permission.PermissionBinder
import kotlinx.android.synthetic.main.activity_images.*


class ImagesExampleActivity : AppCompatActivity() {

    private lateinit var imagesBinder: ImagesBinder
    private lateinit var permissionBinder: PermissionBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)
        val binderExampleApplication = application as BinderExampleApplication
        imagesBinder = binderExampleApplication.imagesBinder
        permissionBinder = binderExampleApplication.permissionBinder

        gallery.setOnClickListener {
            imagesBinder.pickImageFromGallery { imageFile ->
                image.setImageURI(Uri.fromFile(imageFile))
            }
        }
        thumbnail_camera.setOnClickListener {
            imagesBinder.takeThumbnailFromCamera("rationale text for active permission", "settings")
            { bitmap ->
                image.setImageBitmap(bitmap)
            }
        }
        multiple_gallery.setOnClickListener {
            imagesBinder.multiplePickFromGallery {
                Toast.makeText(this, it.size.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        full_size_from_camera_internal_storage.setOnClickListener {
            permissionBinder.activePermission(Manifest.permission.CAMERA,
                    "rationale text for camera permission") { granted ->
                imagesBinder.takeFullSizeFromCamera {
                    image.setImageURI(Uri.fromFile(it))
                }

            }
        }
        full_size_from_camera_external_storage.setOnClickListener {
            permissionBinder.activePermission(Manifest.permission.CAMERA,
                    "rationale text for camera permission") { granted ->
                if (granted) {
                    permissionBinder.activePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            "rationale text for write external storage permission") {
                        if (it) {
                            val settings = FilesPathSettings(application.applicationContext.packageName + ".provider")
                            imagesBinder.takeFullSizeFromCamera(settings) { file ->
                                image.setImageURI(Uri.fromFile(file))
                            }
                        }
                    }
                }
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
