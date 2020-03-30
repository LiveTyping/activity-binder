package com.livetyping.images

import android.app.Activity
import android.content.Intent
import androidx.annotation.XmlRes
import com.livetyping.core.Binder
import com.livetyping.images.photo.PhotoRequest


class ImagesBinder(private val providerAuthority: String,
                   @XmlRes private val paths: Int) : Binder() {

    private val requests: MutableMap<Int, ImageRequest<out Any>> by lazy {
        mutableMapOf<Int, ImageRequest<out Any>>()
    }

    private val waitedContextRequests: MutableMap<Int, ImageRequest<out Any>> by lazy {
        mutableMapOf<Int, ImageRequest<out Any>>()
    }

    fun <T : Any> requestPhoto(request: ImageRequest<out T>, result: (T) -> Unit) {
        if (request is PhotoRequest) {
            request.paths = paths
            request.providerAuthority = providerAuthority
        }
        request.resultFunction = result
        imageRequest(request)
    }

    private fun imageRequest(request: ImageRequest<out Any>) {
        val attachedObject = getCurrentActivity()
        if (attachedObject == null) {
            waitedContextRequests[request.requestCode] = request
        } else {
            requests[request.requestCode] = request
            request.request(attachedObject)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, activity: Activity) {
        if (resultCode == Activity.RESULT_OK) {
            val request = requests[requestCode]
            request?.let { request.activityResult(activity, data) }
        }
    }

    override fun onActivityStarted(activity: Activity?) {
        super.onActivityStarted(activity)
        waitedContextRequests.isNotEmpty().let { notEmpty ->
            if (notEmpty) {
                waitedContextRequests.forEach { imageRequest(it.value) }
                waitedContextRequests.clear()
            }
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //nothing
    }

}
