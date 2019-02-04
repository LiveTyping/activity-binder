package com.livetyping.images.test

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import com.livetyping.images.ImageHeaderParser
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


abstract class TestImageRequest<T>(protected val result: (T) -> Unit) {

    abstract val requestCode: Int

    abstract fun request(attachedObject: Activity)

    abstract fun activityResult(activity: Activity, data: Intent?)

    protected fun saveToProjectFiles(activity: Activity, uri: Uri): File {
        val contentResolver = activity.application.contentResolver
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val storageDir = File(activity.application.cacheDir, "images") // 'images' initialized in default_file_path.xml
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        val tempFile = File.createTempFile(uri.lastPathSegment.toString(), ".jpg", storageDir)
        val out = FileOutputStream(tempFile)
        val openInputStream = contentResolver.openInputStream(uri)
        val angle = getRotationAngle(openInputStream)
        openInputStream?.close()
        val orientationMatrix = Matrix()
        orientationMatrix.postRotate(angle.toFloat())
        val picture = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, orientationMatrix, false)
        picture.compress(Bitmap.CompressFormat.JPEG, 100, out)
        picture.recycle()
        out.close()
        return tempFile
    }

    protected fun getRotationAngle(stream: InputStream): Int {
        val rotation = ImageHeaderParser(stream).orientation
        return when (rotation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }
}
