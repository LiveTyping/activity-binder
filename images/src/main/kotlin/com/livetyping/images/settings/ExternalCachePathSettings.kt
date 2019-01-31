package com.livetyping.images.settings

import android.content.Context
import java.io.File

/**
 * crete file in /storage/emulated/0/Android/data/{applicationId}/cache/attrPath
 */
class ExternalCachePathSettings(override val attrName: String,
                                override val attrPath: String?,
                                override val fileName: String = "file_in_external_cache_dir")
    : TakePhotoSettings() {


    override fun getRootPath(context: Context) = context.externalCacheDir.path
}
