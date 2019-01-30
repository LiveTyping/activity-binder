package com.livetyping.activitybinder

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.livetyping.images.ImagesBinder
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


        file_path.setOnClickListener {
            imagesBinder.takeFullSizeFromCamera("images") {
                image.setImageURI(Uri.fromFile(it))
            }
        }

        default_provider.setOnClickListener {
            imagesBinder.takeFullSizeFromCamera { file ->
                image.setImageURI(Uri.fromFile(file))
            }
        }

        cach_path.setOnClickListener {
            permissionBinder.passivePermission(Manifest.permission.CAMERA) {
                imagesBinder.takeFullSizeFromCamera("cache_files") { file ->
                    image.setImageURI(Uri.fromFile(file))
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
