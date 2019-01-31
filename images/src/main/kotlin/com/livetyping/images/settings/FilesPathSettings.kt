package com.livetyping.images.settings

import android.content.Context

/**
 * crete file in data/data/{applicationId}/files/additionalPath
 */
class FilesPathSettings(override val attrName: String = "images",
                        override val additionalPath: String? = null,
                        override val fileName: String = "file_in_app_files_dir")
    : TakePhotoSettings() {

    override val pathAttr: String
        get() = "files-path"

    override fun getRootPath(context: Context) = context.filesDir.path
}
