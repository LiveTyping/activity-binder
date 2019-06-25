package com.livetyping.images.photo.filecreator

import android.content.Context

/**
 * crete file in data/data/{applicationId}/files/additionalPath
 */
internal class FilesPathCreator(
        attrName: String,
        additionalPath: String?,
        fileName: String = "file_in_app_files_dir"
) : FileCreator(attrName, additionalPath, fileName) {

    override val pathAttr: String
        get() = "files-path"

    override fun getRootPath(context: Context) = context.filesDir.path
}
