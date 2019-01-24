package com.livetyping.images

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream


internal abstract class ImageRequest<T>(protected val result: (T) -> Unit) {

    fun makeRequest(activity: Activity) {
        concreteMakeRequest(activity)
    }

    fun activityResult(activity: Activity, data: Intent) {
        concreteResult(activity, data)
    }

    protected abstract fun concreteMakeRequest(activity: Activity)

    protected abstract fun concreteResult(activity: Activity, data: Intent)


    abstract fun requestCode(): Int

    protected fun fileFromUri(activity: Activity, uri: Uri): File {
        val contentResolver = activity.application.contentResolver
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val storageDir = activity.application.getExternalFilesDir(ImagesBinder.TEMP_CATALOG_PATH)
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val tempFile = File.createTempFile("temp", ".jpg", storageDir)
        val out = FileOutputStream(tempFile)
        val rotation = ImageHeaderParser(contentResolver.openInputStream(uri)).orientation
        val angle = when (rotation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
        val orientationMatrix = Matrix()
        orientationMatrix.postRotate(angle.toFloat())
        val picture = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, orientationMatrix, false)
        picture.compress(Bitmap.CompressFormat.JPEG, 100, out)
        picture.recycle()
        out.close()
        return tempFile
    }
}
