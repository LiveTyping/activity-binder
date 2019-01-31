package com.livetyping.images.settings

import android.content.Context
import android.os.Environment

/**
 * crete file in storage/emulated/0/files/attrPath
 */
class ExternalPathSettings(override val attrName: String,
                           override val attrPath: String?,
                           override val fileName: String = "file_in_external_dir")
    : TakePhotoSettings() {

    override fun getRootPath(context: Context) = Environment.getExternalStorageDirectory().path
}
