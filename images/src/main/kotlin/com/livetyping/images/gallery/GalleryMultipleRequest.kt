package com.livetyping.images.gallery

import android.app.Activity
import android.content.Intent
import java.io.File


class GalleryMultipleRequest(chooserText: String? = null, result: (List<File>) -> Unit)
    : GalleryRequest<List<File>>(chooserText, result) {

    override val requestCode: Int
        get() = 2233

    override fun request(attachedObject: Activity) {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startIntentConsideringChooserText(intent, attachedObject)
    }

    override fun activityResult(attachedObject: Activity, data: Intent?) {
        data?.let {
            val clipData = it.clipData
            if (clipData != null) {
                val itemCount = clipData.itemCount
                if (itemCount > 0) {
                    val list = ArrayList<File>(itemCount)
                    (0 until itemCount)
                            .mapTo(list) { saveToProjectFiles(attachedObject, clipData.getItemAt(it).uri) }
                    result(list)
                }
            } else {
                val list = ArrayList<File>(1)
                list.add(saveToProjectFiles(attachedObject, it?.data))
                result(list)
            }
        }
    }
}
