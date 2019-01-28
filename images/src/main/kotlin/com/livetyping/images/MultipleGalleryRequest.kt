package com.livetyping.utils.image

import android.app.Activity
import android.content.Intent
import com.livetyping.images.ImageRequest
import java.io.File


internal class MultipleGalleryRequest(result: (List<File>) -> Unit) : ImageRequest<List<File>>(result) {

    override fun concreteMakeRequest(activity: Activity) {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode())
    }

    override fun concreteResult(activity: Activity, data: Intent?) {
        data?.let {
            val clipData = it.clipData
            if (clipData != null) {
                val itemCount = clipData.itemCount
                if (itemCount > 0) {
                    val list = ArrayList<File>(itemCount)
                    (0 until itemCount)
                            .mapTo(list) { fileFromUri(activity, clipData.getItemAt(it).uri) }
                    result(list)
                }
            } else {
                    val list = ArrayList<File>(1)
                    list.add(fileFromUri(activity, it?.data))
                    result(list)

            }
        }


    }

    override fun requestCode(): Int = 2233

}
