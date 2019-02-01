package com.livetyping.images.settings

import android.content.Context


/**
 * crete file in data/data/{applicationId}/cache/additionalPath
 */
class CachePathSettings(override val attrName: String,
                        override val additionalPath: String? = null,
                        override val fileName: String = "file_in_cache_dir")
    : TakePhotoSettings() {

    override val pathAttr: String
        get() = "cache-path"


    override fun getRootPath(context: Context) = context.cacheDir.path

}
