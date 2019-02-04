package com.livetyping.images.test.impl

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import com.livetyping.images.test.TestImageRequest
import java.io.File


class GallerySilngleRequest(result: (File) -> Unit) : TestImageRequest<File>(result) {

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
        attachedObject.startActivityForResult(photoPickerIntent, requestCode)
    }

    override fun activityResult(activity: Activity, data: Intent?) {
        if (data != null && data.data != null) {
            result(saveToProjectFiles(activity, data.data!!))
        }

    }
}
