package com.livetyping.images

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import com.livetyping.core.Binder
import com.livetyping.permission.PermissionBinder
import com.livetyping.utils.image.CameraRequest
import com.livetyping.utils.image.GallerySingleRequest
import com.livetyping.utils.image.MultipleGalleryRequest
import java.io.File


class ImagesBinder : Binder() {
    private val requests: MutableMap<Int, ImageRequest<out Any>> = mutableMapOf()
    private val waitedContextRequests: MutableMap<Int, ImageRequest<out Any>> = mutableMapOf()
    private val permissionBinder = PermissionBinder()

    internal companion object {
        internal val TEMP_CATALOG_PATH = Environment.DIRECTORY_PICTURES + "/" + "tmp" + "/"
    }


    fun pickImageFromGallery(result: (File) -> Unit) {
        val gallerySingleRequest = GallerySingleRequest(result)
        imageRequest(gallerySingleRequest)
    }

    fun multiplePickFromGallery(result: (List<File>) -> Unit) {
        imageRequest(MultipleGalleryRequest(result))
    }

    fun takeThumbnailFromCamera(rationaleText: String, settingsButtonText: String, result: (Bitmap) -> Unit) {
        permissionBinder.activePermission(Manifest.permission.CAMERA, rationaleText, settingsButtonText) {
            if (it) {
                val cameraRequest = CameraRequest(result)
                imageRequest(cameraRequest)
            }
        }
    }

    fun takeFullSizeFromCamera(rationaleText: String, settingsButtonText: String, result: (Uri) -> Unit) {
        permissionBinder.activePermission(Manifest.permission.CAMERA, rationaleText, settingsButtonText) {
            if (it) {
                val cameraRequest = FullSizePhotoRequest(result)
                imageRequest(cameraRequest)
            }
        }
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, activity: Activity) {
//        if (resultCode == Activity.RESULT_OK) {
        val request = requests[requestCode]
        request?.let { it.activityResult(activity, Intent()) }
        data?.let { request?.activityResult(activity, data) }
//        }
        permissionBinder.onActivityResult(requestCode, data, activity)
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionBinder.onRequestPermissionResult(requestCode, grantResults)
    }

    private fun imageRequest(imageRequest: ImageRequest<out Any>) {
        val attachedObject = getAttachedObject()
        if (attachedObject == null) {
            waitedContextRequests.put(imageRequest.requestCode(), imageRequest)
        } else {
            val requestCode = imageRequest.requestCode();
            requests.put(requestCode, imageRequest)
            imageRequest.makeRequest(attachedObject)
        }

    }

    override fun attach(obj: Activity) {
        super.attach(obj)
        permissionBinder.attach(obj)
        waitedContextRequests.isNotEmpty().let {
            waitedContextRequests.forEach {
                imageRequest(it.value)
            }.let {
                waitedContextRequests.clear()
            }

        }
    }

    override fun detach(obj: Activity) {
        permissionBinder.detach(obj)
        super.detach(obj)
    }
}
