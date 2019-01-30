package com.livetyping.images

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.livetyping.images.settings.TakePhotoSettings
import java.io.File
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
        function.invoke(fileFromUri(activity, mCurrentPhotoPath))
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
