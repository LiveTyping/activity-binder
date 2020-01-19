package com.livetyping.core

import android.content.Intent
import kotlin.random.Random


class NewBinder : Binder() {

    private val activityResultRequests by lazy {
        mutableMapOf<Int, BinderRequest<*>>()
    }
    private val permissionResultRequests by lazy {
        mutableMapOf<Int, BinderRequest<*>>()
    }

    fun request(request: BinderRequest<*>) {
        request.requestCode = generateRequestCode()
        activityResultRequests[request.requestCode] = request
        permissionResultRequests[request.requestCode] = request
        getAttachedObject()?.let { request.request(it) }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activityResultRequests[requestCode]?.onActivityResult(resultCode, data)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionResultRequests[requestCode]?.onRequestPermissionsResult(permissions, grantResults)
    }


    //TODO need generator for request codes
    private val random = Random(100)

    private fun generateRequestCode(): Int {
        return random.nextInt(100) + 100
    }
}