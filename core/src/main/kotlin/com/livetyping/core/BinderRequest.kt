package com.livetyping.core

import android.app.Activity
import android.content.Intent


abstract class BinderRequest<T> {

    var requestCode: Int = 0

    internal fun onActivityResult(resultCode: Int, data: Intent?) {
        //for override
    }

    open fun onRequestPermissionsResult(
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //for override
    }

    abstract fun request(activity: Activity)
}