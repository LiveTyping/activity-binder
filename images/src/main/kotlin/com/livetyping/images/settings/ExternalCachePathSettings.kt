package com.livetyping.images.settings

import android.content.Context
import java.io.File

/**
 * crete file in /storage/emulated/0/Android/data/{applicationId}/cache/attrPath
 */
class ExternalCachePathSettings(override val providerAuthority: String,
                                override val attrName: String,
                                override val attrPath: String?,
                                override val fileName: String = "temp_external_cache_file") : TakePhotoSettings() {

    override fun getFilePath(context: Context): File {
        val externalFilesDir = context.externalCacheDir
        return if (attrPath == null) {
            File(externalFilesDir.path)
        } else {
            File(externalFilesDir.path, attrPath)
        }
    }
}
