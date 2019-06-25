package com.livetyping.images.photo.filecreator

import android.content.Context


/**
 * crete file in data/data/{applicationId}/cache/additionalPath
 */
internal class CachePathCreator(
        attrName: String,
        additionalPath: String? = null,
        fileName: String = "file_in_cache_dir"
) : FileCreator(attrName, additionalPath, fileName) {

    override val pathAttr: String
        get() = "cache-path"

    override fun getRootPath(context: Context) = context.cacheDir.path
}
