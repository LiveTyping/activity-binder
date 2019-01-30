package com.livetyping.images.settings

import android.content.Context
import java.io.File

/**
 * crete file in data/data/{applicationId}/files/attrPath
 */
class FilesPathSettings(override val attrName: String = "images/",
                        override val attrPath: String? = null,
                        override val fileName: String = "tempFileInFilesPath")
    : TakePhotoSettings() {


    override fun getFilePath(context: Context): File {
        return if (attrPath == null) File(context.filesDir.path) else File(context.filesDir, attrPath)
    }
}
