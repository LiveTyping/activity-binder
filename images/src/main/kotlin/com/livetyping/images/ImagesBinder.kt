package com.livetyping.images

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import androidx.annotation.XmlRes
import com.livetyping.core.Binder
import com.livetyping.images.settings.DefaultTakePhotoSettings
import com.livetyping.images.settings.FilesFactory
import com.livetyping.images.settings.TakePhotoSettings
import com.livetyping.permission.PermissionBinder
import com.livetyping.utils.image.CameraRequest
import com.livetyping.utils.image.GallerySingleRequest
import com.livetyping.utils.image.MultipleGalleryRequest
import java.io.File


class ImagesBinder(private val providerAuthority: String, @XmlRes paths: Int) : Binder() {
    private val requests: MutableMap<Int, ImageRequest<out Any>> = mutableMapOf()
    private val waitedContextRequests: MutableMap<Int, ImageRequest<out Any>> = mutableMapOf()
    private val permissionBinder = PermissionBinder()
    private val filesFactory: FilesFactory by lazy {
        FilesFactory(providerAuthority, paths)
    }

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



    fun takeFullSizeFromCamera(attrName: String? = null, result: (File) -> Unit) {
        getAttachedObject()?.let { activity ->
            val createSettings = filesFactory.createSettings(activity, attrName ?: "")
            takeFullSizeFromCamera(createSettings) {
                result.invoke(it)
            }
        }
    }

    fun takeFullSizeFromCamera(result: (File) -> Unit) {
        getAttachedObject()?.let {
            takeFullSizeFromCamera(DefaultTakePhotoSettings()) { file ->
                result.invoke(file)
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, activity: Activity) {
        if (resultCode == Activity.RESULT_OK) {
            val request = requests[requestCode]
            request?.let { request.activityResult(activity, data) }
        }
        permissionBinder.onActivityResult(requestCode, data, activity)
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionBinder.onRequestPermissionResult(requestCode, grantResults)
    }

    private fun takeFullSizeFromCamera(settings: TakePhotoSettings? = null, result: (File) -> Unit) {
        val takePhotoSettings = settings ?: DefaultTakePhotoSettings()
        val cameraRequest = FullSizePhotoRequest(takePhotoSettings, result)
        imageRequest(cameraRequest)
    }

    private fun imageRequest(imageRequest: ImageRequest<out Any>) {
        val attachedObject = getAttachedObject()
        if (attachedObject == null) {
            waitedContextRequests.put(imageRequest.requestCode(), imageRequest)
        } else {
            val requestCode = imageRequest.requestCode()
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
