package com.livetyping.images.gallery

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import com.livetyping.images.ImageRequest
import java.io.File
import java.io.FileOutputStream


abstract class GalleryRequest<T>(chooserText: String? = null, result: (T) -> Unit)
    : ImageRequest<T>(chooserText, result) {

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
}
