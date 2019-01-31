package com.livetyping.images.settings

import android.content.Context
import java.io.File

/**
 * crete file in data/data/{applicationId}/files
 */
internal class DefaultTakePhotoSettings : TakePhotoSettings() {

    override val attrName = "images"
    override val attrPath: String? = null
    override val fileName = "file_in_app_files_dir"

    override fun getRootPath(context: Context) = context.filesDir.path


}
