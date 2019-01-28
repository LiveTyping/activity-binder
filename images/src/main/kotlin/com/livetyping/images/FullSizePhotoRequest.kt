package com.livetyping.images

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


internal class FullSizePhotoRequest(private val photoSettings: TakePhotoSettings,
                                    private val function: (filePath: File) -> Unit) : ImageRequest<File>(function) {

    private lateinit var mCurrentPhotoPath: Uri

    override fun concreteMakeRequest(activity: Activity) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(activity.packageManager)?.also {
                val imageFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                imageFile?.let {
                    it.deleteOnExit()
                    mCurrentPhotoPath = FileProvider.getUriForFile(
                            activity,
                            photoSettings.providerPath(),
                            it)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath)
                    activity.startActivityForResult(intent, requestCode())

                }
            }
        }
    }

    override fun concreteResult(activity: Activity, data: Intent) {
        function.invoke(fileFromUri(activity, mCurrentPhotoPath))
    }

    override fun requestCode() = 9467

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? =
                Environment.getExternalStorageDirectory()
        val fileName = timeStamp + "_tempfile"
        return File.createTempFile(
                fileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        )
    }
}
