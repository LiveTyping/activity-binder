package com.livetyping.images.gallery

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import com.livetyping.images.ImageRequest
import java.io.File


class GallerySingleRequest(chooserText: String? = null, result: (File) -> Unit)
    : ImageRequest<File>(chooserText, result) {

    override val requestCode: Int
        get() = 3322

    override fun request(attachedObject: Activity) {
        val photoPickerIntent: Intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            with(photoPickerIntent) {
                addCategory(Intent.CATEGORY_OPENABLE)
                setType("image/*")
            }
        } else {
            photoPickerIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            with(photoPickerIntent) {
                setType("image/*")
            }
        }
        startIntentConsideringChooserText(photoPickerIntent, attachedObject)
    }

    override fun activityResult(attachedObject: Activity, data: Intent?) {
        if (data != null && data.data != null) {
            result(saveToProjectFiles(attachedObject, data.data!!))
        }

    }
}
