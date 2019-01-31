package com.livetyping.images.settings

import android.content.Context
import java.io.File

/**
 * crete file in storage/emulated/0/Android/{applicationId}/files/attrPath
 */
class ExternalFilesPathSettings(override val attrName: String,
                                override val attrPath: String?,
                                override val fileName: String = "file_in_external_files_dir")
    : TakePhotoSettings() {

    override fun getRootPath(context: Context) = context.getExternalFilesDir(null).path
}
