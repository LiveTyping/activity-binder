package com.livetyping.images.settings

import android.content.Context
import android.os.Environment

/**
 * crete file in storage/emulated/0/files/additionalPath
 */
class ExternalPathSettings(attrName: String,
                           additionalPath: String?,
                           fileName: String = "file_in_external_dir")

    : TakePhotoSettings(attrName, additionalPath, fileName) {

    override val pathAttr: String
        get() = "external-path"

    override fun getRootPath(context: Context) = Environment.getExternalStorageDirectory().path
}
