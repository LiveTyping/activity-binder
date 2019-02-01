package com.livetyping.images.settings

import android.content.Context
import android.os.Environment

/**
 * crete file in storage/emulated/0/files/additionalPath
 */
class ExternalPathSettings(override val attrName: String,
                           override val additionalPath: String? = null,
                           override val fileName: String = "file_in_external_dir")
    : TakePhotoSettings() {

    override val pathAttr: String
        get() = "external-path"

    override fun getRootPath(context: Context) = Environment.getExternalStorageDirectory().path
}
