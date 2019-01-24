package com.livetyping.utils.image

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import com.livetyping.images.ImageRequest
import java.io.File


internal class GallerySingleRequest(result: (File) -> Unit) : ImageRequest<File>(result) {

    override fun concreteMakeRequest(activity: Activity) {
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
        activity.startActivityForResult(photoPickerIntent, requestCode())
    }

    override fun concreteResult(activity: Activity, data: Intent) {
        result(fileFromUri(activity, data.data))
    }

    override fun requestCode(): Int = 2233

}
