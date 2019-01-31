package com.livetyping.images.settings

import android.content.Context

/**
 * crete file in /storage/emulated/0/Android/data/{applicationId}/cache/additionalPath
 */
class ExternalCachePathSettings(override val attrName: String,
                                override val additionalPath: String?,
                                override val fileName: String = "file_in_external_cache_dir")
    : TakePhotoSettings() {

    override val pathAttr: String
        get() = "external-cache-path"

    override fun getRootPath(context: Context) = context.externalCacheDir.path
}
