package com.livetyping.images

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.livetyping.images.settings.TakePhotoSettings
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


internal class FullSizePhotoRequest(private val providerAuthority: String,
                                    val photoSettings: TakePhotoSettings,
                                    private val function: (filePath: File) -> Unit)
    : ImageRequest<File>(function) {

    private lateinit var mCurrentPhotoPath: Uri

    override fun concreteMakeRequest(activity: Activity) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(activity.packageManager)?.also {
                val imageFile: File? = try {
                    createImageFile(activity)
                } catch (ex: IOException) {
                    null
                }
                imageFile?.let {
                    it.deleteOnExit()
                    mCurrentPhotoPath = FileProvider.getUriForFile(activity,
                            providerAuthority,
                            it)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath)
                    activity.startActivityForResult(intent, requestCode())

                }
            }
        }
    }

    override fun concreteResult(activity: Activity, data: Intent?) {
        val contentResolver = activity.application.contentResolver
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, mCurrentPhotoPath)
        val rotation = ImageHeaderParser(contentResolver.openInputStream(mCurrentPhotoPath)).orientation
        val angle = when (rotation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
        val file = File(photoSettings.getRootPath(activity), "${photoSettings.fileName}.jpg")
        file.delete()
        file.createNewFile()
        val out = FileOutputStream(file)
        val orientationMatrix = Matrix()
        orientationMatrix.postRotate(angle.toFloat())
        val picture = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, orientationMatrix, false)
        picture.compress(Bitmap.CompressFormat.JPEG, 100, out)
        picture.recycle()
        out.close()
        function.invoke(file)

    }

    override fun requestCode() = 9467

    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {
        // Create an image file name
        val storageDir = photoSettings.getFilePath(context)
        storageDir?.let {
            if (!storageDir.exists()) {
                storageDir.mkdirs()
            }
            val fileName = photoSettings.fileName
            val tempFile = File(it.absolutePath, "$fileName.jpg")
            tempFile.createNewFile()
            return tempFile
        }
    }
}
