package com.livetyping.images.photo.filecreator

import android.content.Context
import android.os.Environment

/**
 * crete file in storage/emulated/0/files/additionalPath
 */
internal class ExternalPathCreator(
        attrName: String,
        additionalPath: String?,
        fileName: String = "file_in_external_dir"
) : FileCreator(attrName, additionalPath, fileName) {

    override val pathAttr: String
        get() = "external-path"

    override fun getRootPath(context: Context) = Environment.getExternalStorageDirectory().path
}
