package com.livetyping.images.settings

import android.content.Context

/**
 * crete file in data/data/{applicationId}/files
 */
class DefaultTakePhotoSettings : TakePhotoSettings() {

    override val attrName = "images"
    override val additionalPath: String? = null
    override val fileName = "file_in_app_files_dir"

    override val pathAttr: String
        get() = "files-path"


    override fun getRootPath(context: Context) = context.filesDir.path


}
