package com.livetyping.activitybinder

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.livetyping.images.ImagesBinder
import com.livetyping.images.settings.*
import com.livetyping.permission.PermissionBinder
import kotlinx.android.synthetic.main.activity_full_size_images.*


class FullSizeImagesActivity : AppCompatActivity() {

    private lateinit var imagesBinder: ImagesBinder
    private lateinit var permissionBinder: PermissionBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_size_images)
        val binderExampleApplication = application as BinderExampleApplication
        imagesBinder = binderExampleApplication.imagesBinder
        permissionBinder = binderExampleApplication.permissionBinder

        default_provider.setOnClickListener {
            permissionBinder.passivePermission(Manifest.permission.CAMERA) {
                imagesBinder.takeFullSizeFromCamera { file ->
                    image.setImageURI(Uri.fromFile(file))
                }
            }

        }

        file_path.setOnClickListener {
            permissionBinder.passivePermission(Manifest.permission.CAMERA) { cameraGranted ->
                if (cameraGranted) {
                    permissionBinder.passivePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) { storageGranted ->
                        if (storageGranted) {
                            val settings = FilesPathSettings("images", additionalPath = "my_folder", fileName = "my_file")
                            imagesBinder.takeFullSizePhotoFromCamera(settings) { file ->
                                image.setImageURI(Uri.fromFile(file))
                            }
                        }
                    }
                }
            }
        }

        cach_path.setOnClickListener {
            permissionBinder.passivePermission(Manifest.permission.CAMERA) {
                val cachePathSettings = CachePathSettings("cache_files", fileName = "my_file_in_cache")
                imagesBinder.takeFullSizePhotoFromCamera(cachePathSettings) { file ->
                    image.setImageURI(Uri.fromFile(file))
                }
            }
        }
//
        external_path.setOnClickListener {
            permissionBinder.passivePermission(Manifest.permission.CAMERA) { cameraGranted ->
                if (cameraGranted) {
                    permissionBinder.passivePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) { storageGranted ->
                        if (storageGranted) {
                            val externalPathSettings = ExternalFilesPathSettings("external_files")
                            imagesBinder.takeFullSizePhotoFromCamera(externalPathSettings) { file ->
                                image.setImageURI(Uri.fromFile(file))
                            }
                        }
                    }
                }
            }
        }

        external_files_path.setOnClickListener {
            permissionBinder.passivePermission(Manifest.permission.CAMERA) { cameraGranted ->
                if (cameraGranted) {
                    permissionBinder.passivePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) { storageGranted ->
                        if (storageGranted) {
                            val settings = ExternalFilesPathSettings("external_app_files_path")
                            imagesBinder.takeFullSizePhotoFromCamera(settings) { file ->
                                image.setImageURI(Uri.fromFile(file))
                            }
                        }
                    }
                }
            }
        }
//
        external_cache_path.setOnClickListener {
            permissionBinder.passivePermission(Manifest.permission.CAMERA) { cameraGranted ->
                if (cameraGranted) {
                    permissionBinder.passivePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) { storageGranted ->
                        if (storageGranted) {
                            val settings = ExternalCachePathSettings("external_app_cache_path")
                            imagesBinder.takeFullSizePhotoFromCamera(settings) { file ->
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
