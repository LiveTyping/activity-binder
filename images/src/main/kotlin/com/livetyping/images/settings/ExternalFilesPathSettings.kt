package com.livetyping.images.settings

import android.content.Context
import java.io.File

/**
 * crete file in storage/emulated/0/Android/{applicationId}/files/attrPath
 */
class ExternalFilesPathSettings(override val providerAuthority: String,
                                override val attrName: String,
                                override val attrPath: String?,
                                override val fileName: String = "tmp_external_app_file")
    : TakePhotoSettings() {

    override fun getFilePath(context: Context): File {
        val externalFilesDir = context.getExternalFilesDir(null)
        return if (attrPath == null) {
            File(externalFilesDir.path)
        } else {
            File(externalFilesDir.path, attrPath)
        }
    }
}
