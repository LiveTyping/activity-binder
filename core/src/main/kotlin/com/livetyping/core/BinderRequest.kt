package com.livetyping.core

import android.app.Activity
import android.content.Intent


abstract class BinderRequest<T> {

    protected var requestCode: Int = 0
    protected lateinit var block: (result: T) -> Unit

    internal fun setRequestCode(requestCode: Int) {
        this.requestCode = requestCode
    }

    internal fun setBlock(block: (result: T) -> Unit) {
        this.block = block
    }

    internal fun internalActivityResult(resultCode: Int, data: Intent?) {
        onActivityResult(resultCode, data)
    }

    internal fun internalPermissionResult(
        activity: Activity,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        onRequestPermissionsResult(activity, permissions, grantResults)
    }

    internal fun internalRequest(activity: Activity) = request(activity)

    protected open fun onActivityResult(resultCode: Int, data: Intent?) {
        //for override
    }

    protected open fun onRequestPermissionsResult(
        activity: Activity,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //for override
    }

    protected abstract fun request(activity: Activity)
}