package com.livetyping.images.photo.filecreator

import android.content.Context

/**
 * crete file in /storage/emulated/0/Android/data/{applicationId}/cache/additionalPath
 */
internal class ExternalCachePathCreator(
        attrName: String,
        additionalPath: String? = null,
        fileName: String = "file_in_external_cache_dir"
) : FileCreator(attrName, additionalPath, fileName) {

    override val pathAttr: String
        get() = "external-cache-path"

    override fun getRootPath(context: Context) = context.externalCacheDir.path
}
