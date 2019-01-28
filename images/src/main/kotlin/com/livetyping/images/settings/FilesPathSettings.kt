package com.livetyping.images.settings

import android.content.Context
import android.os.Environment


class FilesPathSettings(override val providerAuthority: String,
                        override val attrName: String = "images/",
                        override val attrPath: String = ".",
                        override val fileName: String = "tempFileInFilesPath") : TakePhotoSettings() {

    override val tagPath = "files-path"

    override fun getFilePath(context: Context) = context.filesDir
}
