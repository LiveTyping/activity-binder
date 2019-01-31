package com.livetyping.images.settings

import android.content.Context
import java.io.File


abstract class TakePhotoSettings {

    abstract val attrName: String

    abstract val attrPath: String?

    abstract val fileName: String

    internal fun getFilePath(context: Context): File {
        val rootPath = getRootPath(context)
        val path = if (attrPath == null) File(rootPath) else File(rootPath, attrPath)
        if (path.exists().not()) {
            path.mkdirs()
        }
        return path
    }

    internal abstract fun getRootPath(context: Context): String
}
