package com.livetyping.images

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.XmlRes
import androidx.core.content.FileProvider
import com.livetyping.images.settings.TakePhotoSettings
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


internal class FullSizePhotoRequest(private val providerAuthority: String,
                                    @XmlRes private val paths: Int,
                                    val photoSettings: TakePhotoSettings,
                                    private val function: (filePath: File) -> Unit)
    : ImageRequest<File>(function) {

    private lateinit var mCurrentPhotoPath: Uri

    override fun concreteMakeRequest(activity: Activity) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(activity.packageManager)?.also {
                val imageFile: File? = try {
                    photoSettings.getImageFile(activity, paths)
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
        val rotationInputStream = contentResolver.openInputStream(mCurrentPhotoPath)
        //TODO check for null
        val angle = getRotationAngle(rotationInputStream)
        rotationInputStream?.close()
        val imageFile = photoSettings.getImageFile(activity, paths)
        if (angle == 0) {
            function.invoke(imageFile)
        } else {
            imageFile.delete()
            imageFile.createNewFile()
            val out = FileOutputStream(imageFile)
            val orientationMatrix = Matrix()
            orientationMatrix.postRotate(angle.toFloat())
            val picture = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, orientationMatrix, false)
            picture.compress(Bitmap.CompressFormat.JPEG, 100, out)
            picture.recycle()
            out.close()
            function.invoke(imageFile)
        }


    }

    override fun requestCode() = 9467
}
