package com.livetyping.images.settings

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * crete file in storage/emulated/0/files/attrPath
 */
internal class ExternalPathSettings(override val providerAuthority: String,
                                    override val attrName: String,
                                    override val attrPath: String?,
                                    override val fileName: String = "external_path_file")
    : TakePhotoSettings() {

    override fun getFilePath(context: Context): File {
        val externalStorageDirectory = Environment.getExternalStorageDirectory()
        return if (attrPath == null) File(externalStorageDirectory.path) else File(externalStorageDirectory, attrPath)
    }
}
