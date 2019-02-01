package com.livetyping.images.settings

import android.content.Context

/**
 * crete file in storage/emulated/0/Android/{applicationId}/files/additionalPath
 */
class ExternalFilesPathSettings(attrName: String,
                                additionalPath: String? = null,
                                fileName: String = "file_in_external_files_dir")
    : TakePhotoSettings(attrName, additionalPath, fileName) {

    override val pathAttr: String
        get() = "external-files-path"

    override fun getRootPath(context: Context) = context.getExternalFilesDir(null).path
}
