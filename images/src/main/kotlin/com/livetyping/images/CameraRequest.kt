package com.livetyping.utils.image

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import com.livetyping.images.ImageRequest
import java.io.File


internal class CameraRequest(result: (Bitmap) -> Unit) : ImageRequest<Bitmap>(result) {

    override fun concreteMakeRequest(activity: Activity) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivityForResult(takePictureIntent, requestCode())
        }
    }

    override fun concreteResult(activity: Activity, data: Intent?) {
        data?.extras?.let {
            result(it.get("data") as Bitmap)
        }
    }

    override fun requestCode(): Int = 3232

}
