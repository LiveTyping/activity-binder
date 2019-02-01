package com.livetyping.images.settings

import android.content.Context

/**
 * crete file in /storage/emulated/0/Android/data/{applicationId}/cache/additionalPath
 */
class ExternalCachePathSettings(attrName: String,
                                additionalPath: String? = null,
                                fileName: String = "file_in_external_cache_dir")

    : TakePhotoSettings(attrName, additionalPath, fileName) {

    override val pathAttr: String
        get() = "external-cache-path"

    override fun getRootPath(context: Context) = context.externalCacheDir.path
}
