package com.livetyping.images.settings

import android.content.Context

/**
 * crete file in data/data/{applicationId}/files/attrPath
 */
class FilesPathSettings(override val attrName: String = "images/",
                        override val attrPath: String? = null,
                        override val fileName: String = "tempFileInFilesPath")
    : TakePhotoSettings() {

    override fun getRootPath(context: Context) = context.filesDir.path
}
