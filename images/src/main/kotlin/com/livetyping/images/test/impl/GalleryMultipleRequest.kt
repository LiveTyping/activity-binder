package com.livetyping.images.test.impl

import android.app.Activity
import android.content.Intent
import com.livetyping.images.test.TestImageRequest
import java.io.File


class GalleryMultipleRequest(result: (List<File>) -> Unit) : TestImageRequest<List<File>>(result) {

    override val requestCode: Int
        get() = 2233

    override fun request(attachedObject: Activity) {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        // TODO add title as param
        attachedObject.startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode)
    }

    override fun activityResult(activity: Activity, data: Intent?) {
        data?.let {
            val clipData = it.clipData
            if (clipData != null) {
                val itemCount = clipData.itemCount
                if (itemCount > 0) {
                    val list = ArrayList<File>(itemCount)
                    (0 until itemCount)
                            .mapTo(list) { saveToProjectFiles(activity, clipData.getItemAt(it).uri) }
                    result(list)
                }
            } else {
                val list = ArrayList<File>(1)
                list.add(saveToProjectFiles(activity, it?.data))
                result(list)
            }
        }
    }
}
