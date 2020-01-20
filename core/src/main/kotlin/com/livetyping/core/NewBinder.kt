package com.livetyping.core

import android.content.Intent
import kotlin.random.Random


class NewBinder : Binder() {


    private val requests by lazy {
        mutableMapOf<Int, BinderRequest<*>>()
    }

    fun request(request: BinderRequest<*>) {
        val generateRequestCode = generateRequestCode()
        request.setRequestCode(generateRequestCode)
        requests[generateRequestCode] = request
        getAttachedObject()?.let { request.internalRequest(it) }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        requests[requestCode]?.onActivityResult(resultCode, data)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        requests[requestCode]?.onRequestPermissionsResult(permissions, grantResults)
    }


    //TODO need generator for request codes
    private val random = Random(100)

    private fun generateRequestCode(): Int {
        return random.nextInt(100) + 100
    }
}