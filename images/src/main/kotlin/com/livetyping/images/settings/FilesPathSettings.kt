package com.livetyping.images.settings

import android.content.Context
import java.io.File

/**
 * crete file in data/data/{applicationId}/attrPath for file path
 */
class FilesPathSettings(override val providerAuthority: String,
                        override val attrName: String = "images/",
                        override val attrPath: String = ".",
                        override val fileName: String = "tempFileInFilesPath") : TakePhotoSettings() {

    override val tagPath = "files-path"

    override fun getFilePath(context: Context): File {
        return File(context.filesDir, attrPath)
    }
}
