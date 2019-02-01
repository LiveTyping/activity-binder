package com.livetyping.images

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.annotation.XmlRes
import com.livetyping.core.Binder
import com.livetyping.images.settings.DefaultTakePhotoSettings
import com.livetyping.images.settings.TakePhotoSettings
import java.io.File


class ImagesBinder(private val providerAuthority: String,
                   @XmlRes private val paths: Int) : Binder() {

    private val requests: MutableMap<Int, ImageRequest<out Any>> = mutableMapOf()
    private val waitedContextRequests: MutableMap<Int, ImageRequest<out Any>> = mutableMapOf()

    fun pickImageFromGallery(result: (File) -> Unit) {
        val gallerySingleRequest = GallerySingleRequest(result)
        imageRequest(gallerySingleRequest)
    }

    fun multiplePickFromGallery(result: (List<File>) -> Unit) {
        imageRequest(MultipleGalleryRequest(result))
    }

    fun takeThumbnailFromCamera(result: (Bitmap) -> Unit) {
        val cameraRequest = CameraRequest(result)
        imageRequest(cameraRequest)
    }

    fun takeFullSizeFromCamera(result: (File) -> Unit) {
        getAttachedObject()?.let {
            takeFullSizeFromCamera(null) { file ->
                result.invoke(file)
            }
        }
    }

    fun takeFullSizePhotoFromCamera(settings: TakePhotoSettings, result: (File) -> Unit) {
        getAttachedObject()?.let {
            takeFullSizeFromCamera(settings) { file ->
                result.invoke(file)
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, activity: Activity) {
        if (resultCode == Activity.RESULT_OK) {
            val request = requests[requestCode]
            request?.let { request.activityResult(activity, data) }
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //nothing
    }

    private fun takeFullSizeFromCamera(settings: TakePhotoSettings? = null, result: (File) -> Unit) {
        val providerAuthority = if (settings == null) "com.livetyping.images.default_provider" else this.providerAuthority
        val cameraRequest = FullSizePhotoRequest(providerAuthority, paths,
                settings ?: DefaultTakePhotoSettings(),
                result)
        imageRequest(cameraRequest)
    }

    private fun imageRequest(imageRequest: ImageRequest<out Any>) {
        val attachedObject = getAttachedObject()
        if (attachedObject == null) {
            waitedContextRequests[imageRequest.requestCode()] = imageRequest
        } else {
            val requestCode = imageRequest.requestCode()
            requests[requestCode] = imageRequest
            imageRequest.makeRequest(attachedObject)
        }
    }

    override fun attach(obj: Activity) {
        super.attach(obj)
        waitedContextRequests.isNotEmpty().let { notEmpty ->
            if (notEmpty) {
                waitedContextRequests.forEach { imageRequest(it.value) }
                waitedContextRequests.clear()
            }
        }
    }
}
