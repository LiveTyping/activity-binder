package com.livetyping.images

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


internal class FullSizePhotoRequest(private val function: (file: File) -> Unit) : ImageRequest<File>(function) {

    private lateinit var mCurrentPhotoPath: String

    override fun concreteMakeRequest(activity: Activity) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(activity.packageManager)?.also {
                val imageFile: File? = try {
                    createImageFile(activity)
                } catch (ex: IOException) {
                    null
                }
                imageFile?.let {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            activity,
                            activity.applicationContext.packageName + ".provider",
                            imageFile)
                    intent.putExtra(MediaStore.ACTION_IMAGE_CAPTURE, photoURI)
                    activity.startActivityForResult(intent, requestCode())

                }
            }
        }
    }

    override fun concreteResult(activity: Activity, data: Intent) {
        function(File(mCurrentPhotoPath))
    }

    override fun requestCode() = 9467

    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? =
                Environment.getExternalStorageDirectory()
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }
}
