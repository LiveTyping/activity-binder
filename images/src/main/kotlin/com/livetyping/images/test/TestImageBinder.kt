package com.livetyping.images.test

import android.app.Activity
import android.content.Intent
import androidx.annotation.XmlRes
import com.livetyping.core.Binder
import com.livetyping.images.test.impl.PhotoRequest


class TestImageBinder(private val providerAuthority: String,
                      @XmlRes private val paths: Int) : Binder() {

    private val requests: MutableMap<Int, TestImageRequest<out Any>> by lazy {
        mutableMapOf<Int, TestImageRequest<out Any>>()
    }

    private val waitedContextRequests: MutableMap<Int, TestImageRequest<out Any>> by lazy {
        mutableMapOf<Int, TestImageRequest<out Any>>()
    }

    fun requestPhoto(request: TestImageRequest<out Any>) {
        imageRequest(request)
    }

    private fun imageRequest(request: TestImageRequest<out Any>) {
        val attachedObject = getAttachedObject()
        if (attachedObject == null) {
            waitedContextRequests[request.requestCode] = request
        } else {
            requests[request.requestCode] = request
            request.request(attachedObject)
        }
    }

    fun requestPhoto(request: PhotoRequest) {
        //TODO add nullable paths and authority. Error if null
        request.paths = paths
        request.providerAuthority = providerAuthority
        imageRequest(request)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, activity: Activity) {
        if (resultCode == Activity.RESULT_OK) {
            val request = requests[requestCode]
            request?.let { request.activityResult(activity, data) }
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

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //nothing
    }

}
