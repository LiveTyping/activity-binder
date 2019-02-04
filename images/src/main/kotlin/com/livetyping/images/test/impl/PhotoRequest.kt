package com.livetyping.images.test.impl

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.livetyping.images.settings.TakePhotoSettings
import com.livetyping.images.test.TestImageRequest
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


abstract class PhotoRequest(chooserText: String? = null,
                            private val photoSettings: TakePhotoSettings,
                            result: (File) -> Unit)
    : TestImageRequest<File>(chooserText, result) {

    internal lateinit var mCurrentPhotoPath: Uri
    internal lateinit var providerAuthority: String
    internal var paths: Int? = null
    override val requestCode = 9467

    override fun request(attachedObject: Activity) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(attachedObject.packageManager)?.also {
                val imageFile: File? = try {
                    photoSettings.getImageFile(attachedObject, paths!!)
                } catch (ex: IOException) {
                    //TODO add work on exception
                    null
                }
                imageFile?.let {
                    it.deleteOnExit()
                    mCurrentPhotoPath = FileProvider.getUriForFile(attachedObject,
                            providerAuthority,
                            it)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath)
                    startIntentConsideringChooserText(intent, attachedObject)
                }
            }
        }
    }

    override fun activityResult(attachedObject: Activity, data: Intent?) {
        val contentResolver = attachedObject.application.contentResolver
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, mCurrentPhotoPath)
        val rotationInputStream = contentResolver.openInputStream(mCurrentPhotoPath)
        //TODO check for null
        val angle = getRotationAngle(rotationInputStream)
        rotationInputStream?.close()
        val imageFile = photoSettings.getImageFile(attachedObject, paths!!)
        if (angle == 0) {
            result.invoke(imageFile)
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
            result.invoke(imageFile)
        }
    }
}
