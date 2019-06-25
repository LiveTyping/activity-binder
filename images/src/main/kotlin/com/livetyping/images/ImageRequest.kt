package com.livetyping.images

import android.app.Activity
import android.content.Intent
import androidx.exifinterface.media.ExifInterface
import java.io.InputStream


abstract class ImageRequest<T>(private val chooserText: String? = null) {

    abstract val requestCode: Int
    internal lateinit var resultFunction: (T) -> Unit

    abstract fun request(attachedObject: Activity)

    abstract fun activityResult(attachedObject: Activity, data: Intent?)


    protected fun getRotationAngle(stream: InputStream): Int {
        val rotation = ImageHeaderParser(stream).orientation
        return when (rotation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    protected fun startIntentConsideringChooserText(intent: Intent, attachedObject: Activity) {
        if (chooserText == null) {
            attachedObject.startActivityForResult(intent, requestCode)
        } else {
            attachedObject.startActivityForResult(Intent.createChooser(intent, chooserText), requestCode)
        }
    }
}
